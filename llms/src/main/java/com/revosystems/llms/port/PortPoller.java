package com.revosystems.llms.port;

import java.util.function.Consumer;
import java.util.function.Function;

import javax.xml.bind.DatatypeConverter;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortPacketListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
@AllArgsConstructor
public class PortPoller<T> implements SerialPortPacketListener {
	
	private SerialPort port;
	@NonNull private final byte[] request;
	@NonNull private final String portName;
	@NonNull private final Integer responseSize;
	@NonNull private final Consumer<T> callback;
	@NonNull private final PortResolver portResolver;
	@NonNull private final Function<byte[], T> resolver;
	
	public void poll() {
		if(!isConnected()) connect();
		if(isConnected()) write();
	}

	private void write() {
		port.removeDataListener();
		port.addDataListener(this);
		port.writeBytes(request, request.length);
		while(port.bytesAwaitingWrite() > 0) {}
	}
	
	private boolean isConnected() {
		return null != port && port.isOpen() && hasName(portName, port);
	}
	
	private void connect() {
		if(null == port) log.info("Looking up and opening port for first time");
		else if(!port.isOpen()) log.info("Port is not open, looking up and opening it again");
		else if(!hasName(portName, port)) log.info("Port name has changed, looking up and opening appropriate port");
		
		if(null != port) port.removeDataListener();
		port = portResolver.resolve(portName);
		if(null != port) {
			port.setBaudRate(9600);
			port.setNumDataBits(8);
			port.setNumStopBits(1);
			port.setParity(SerialPort.NO_PARITY);
			port.openPort();
			port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 1000);
			port.addDataListener(this);
			log.info("Started listening on port {}", toString(port));
		} else {
			log.error("Could not find any port for name " + portName);
		}
	}
	
	private String toString(SerialPort port) {
		return String.join(" - ", port.getSystemPortName(), port.getDescriptivePortName(), port.getPortDescription());
	}
	
	private boolean hasName(String name, SerialPort port) {
		return port.getDescriptivePortName().toLowerCase().contains(name.toLowerCase());
		
	}

	@Override
	public int getListeningEvents() {
		return SerialPort.LISTENING_EVENT_DATA_RECEIVED | SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
	}

	@Override
	public int getPacketSize() {
		return responseSize;
	}
	
	@Override
	public void serialEvent(SerialPortEvent event) {
		final byte[] response = event.getReceivedData();
		log.debug("Received data from port : {}", DatatypeConverter.printHexBinary(response));
		if(33 > response.length) {
			log.error("Response too short, expected at least {} bytes, got {}", responseSize, response.length);
		} else {
			try {
				callback.accept(resolver.apply(response));
			} catch(Exception e) {
				log.error("Failed to process response", e);
			}
		}
	}
}
