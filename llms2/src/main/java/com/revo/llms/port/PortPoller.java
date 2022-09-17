package com.revo.llms.port;

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
	@Builder.Default private volatile long lastRequestTimestamp = 0;
	
	@NonNull private final String portName;
	@NonNull private final Integer responseSize;
	@NonNull private final Consumer<T> callback;
	@NonNull private final PortResolver portResolver;
	@NonNull private final Function<byte[], T> resolver;
	
	public void poll(final byte[] request) {
		if(!isConnected()) {
			log.debug("Not connected to port, attempting to connect");
			connect();
		}
		if(isConnected()) {
			log.trace("Port is connected, writing request");
			write(request);
		}
	}

	private void write(final byte[] request) {
		port.removeDataListener();
		port.addDataListener(PortPoller.this);
		log.debug("Writing request to port : {}", request);
		port.writeBytes(request, request.length);
		lastRequestTimestamp = System.currentTimeMillis();
		while(port.bytesAwaitingWrite() > 0) Thread.yield();
		log.trace("Written full request to the port");
	}
	
	private boolean isConnected() {
		return null != port && port.isOpen() && hasName(portName, port);
	}
	
	private void connect() {
		if(null == port) log.debug("Looking up and opening port for first time");
		else if(!port.isOpen()) log.info("Port is not open, looking up and opening it again");
		else if(!hasName(portName, port)) log.info("Port name has changed, looking up and opening appropriate port");
		
		if(null != port) port.removeDataListener();
		port = portResolver.resolve(portName);
		if(null != port) {
			port.openPort();
			port.addDataListener(PortPoller.this);
			port.setBaudRate(9600);
			port.setNumDataBits(8);
			port.setNumStopBits(1);
			port.setParity(SerialPort.NO_PARITY);
			port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING | SerialPort.TIMEOUT_WRITE_BLOCKING, 30000, 30000);
			log.info("Started listening on port {}", toString(port));
		} else {
			log.debug("Could not find any port for name " + portName);
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
		try {
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
		} finally {
			lastRequestTimestamp = 0;
		}
	}
}
