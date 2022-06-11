package com.revo.llms.port;

import javax.xml.bind.DatatypeConverter;

import com.fazecast.jSerialComm.SerialPort;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
@AllArgsConstructor
public class PortPoller<T> {
	
	private SerialPort port;
	@Builder.Default private volatile long lastRequestTimestamp = 0;
	
	@NonNull private final String portName;
	@NonNull private final Integer responseSize;
	@NonNull private final PortResolver portResolver;
	@Builder.Default private final int requestTimeout = 1000;
	
	public byte[] poll(final byte[] request) {
		if(!isConnected()) {
			log.debug("Not connected to port, attempting to connect");
			connect();
		}
		if(isConnected()) {
			log.trace("Port is connected, writing request");
			final byte[] buffer = new byte[35];
			port.writeBytes(request, request.length);
			port.readBytes(buffer, 35);
			log.info("{} : {}", DatatypeConverter.printHexBinary(request), 
					DatatypeConverter.printHexBinary(buffer));
			return buffer;
		}
		log.warn("Not connected to port");
		return null;
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
			port.openPort();
			port.setBaudRate(9600);
			port.setNumDataBits(8);
			port.setNumStopBits(1);
			port.setParity(SerialPort.NO_PARITY);
			port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING | SerialPort.TIMEOUT_WRITE_BLOCKING, requestTimeout, requestTimeout);
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
	
}
