package com.revosystems.llms.util;

import java.util.logging.Logger;
import java.util.stream.Stream;

import com.fazecast.jSerialComm.SerialPort;

public class Ports {
	private static final Logger log = Logger.getLogger(Ports.class.getCanonicalName());

	public static String toString(SerialPort port) {
		return String.join(" - ", port.getSystemPortName(), port.getDescriptivePortName(), port.getPortDescription());
	}
	
	public static boolean hasName(final String name, SerialPort port) {
		return port.getDescriptivePortName().toLowerCase().contains(name.toLowerCase());
	}
	
	public static SerialPort findByName(final String name) {
		return Stream.of(SerialPort.getCommPorts())
			.map(port -> {
				log.info(String.format("Found port %s with desc %s", port.getSystemPortName(), port.getDescriptivePortName()));
				return port;
			})
			.filter(port -> hasName(name, port))
			.findAny()
			.orElse(null);
	}
	
}
