package com.revosystems.cbms.util;

import java.util.stream.Stream;

import com.fazecast.jSerialComm.SerialPort;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
			.map(port -> {
				log.info("Found port {} with desc {}", port.getSystemPortName(), port.getDescriptivePortName());
				return port;
			})
			.filter(port -> hasName(name, port))
			.findAny()
			.orElse(null);
	}
	
}
