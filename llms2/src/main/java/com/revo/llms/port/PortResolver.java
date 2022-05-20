package com.revo.llms.port;

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
					log.info(String.format("Found port %s with desc %s - %s", port.getSystemPortName(), 
							port.getDescriptivePortName(), port.getPortDescription()));
					return port;
				})
				.filter(port -> 
					port.getSystemPortName().toLowerCase().contains(name.toLowerCase()) ||
					port.getDescriptivePortName().toLowerCase().contains(name.toLowerCase()) ||
					port.getPortDescription().toLowerCase().contains(name.toLowerCase()))
				.findAny()
				.orElse(null);
	}
	
}
