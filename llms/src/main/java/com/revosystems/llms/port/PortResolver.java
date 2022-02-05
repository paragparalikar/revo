package com.revosystems.llms.port;

import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.fazecast.jSerialComm.SerialPort;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PortResolver {

	public SerialPort resolve(String name) {
		return Stream.of(SerialPort.getCommPorts())
				.map(port -> {
					log.info(String.format("Found port %s with desc %s", port.getSystemPortName(), port.getDescriptivePortName()));
					return port;
				})
				.filter(port -> port.getDescriptivePortName().toLowerCase().contains(name.toLowerCase()))
				.findAny()
				.orElse(null);
	}
	
}
