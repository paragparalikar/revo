package com.revosystems.cbms.util;

import java.util.stream.Stream;

import com.fazecast.jSerialComm.SerialPort;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Ports {

	public String toString(SerialPort port) {
		return String.join(" - ", port.getSystemPortName(), port.getDescriptivePortName(), port.getPortDescription());
	}
	
	public boolean hasName(@NonNull final String name, @NonNull SerialPort port) {
		return port.getDescriptivePortName().toLowerCase().contains(name.toLowerCase());
	}
	
	public SerialPort findByName(@NonNull final String name) {
		return Stream.of(SerialPort.getCommPorts())
			.filter(port -> hasName(name, port))
			.findAny()
			.orElse(null);
	}
	
}
