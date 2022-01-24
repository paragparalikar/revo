package com.revosystems.llms;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.revosystems.llms.util.Ports;
import com.revosystems.llms.util.Strings;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Client implements Runnable, SerialPortDataListener {
	private static final byte[] REQUEST = new byte[]{1,3,0,0,0,8,15,5};

	private SerialPort port;
	
	@Value("${port.name:CH340}")
	private String portName;
	
	@Autowired
	private StationStateChangeService stationStateChangeService;
	
	@Override
	@Scheduled(initialDelay = 1000, fixedRateString = "${poll.delay.millis:1000}")
	public void run(){
		if(!isConnected()) connect();
		if(isConnected()) write();
	}
	
	private void write() {
		log.info("Sending request to port " + port.getDescriptivePortName());
		log.info("Request data : " + Long.toHexString(ByteBuffer.wrap(REQUEST).getLong()));
		port.removeDataListener();
		port.addDataListener(this);
		port.writeBytes(REQUEST, 8);
		while(port.bytesAwaitingWrite() > 0) {}
		log.info("Request sent successfully to port " + port.getDescriptivePortName());
	}
	
	private boolean isConnected() {
		return null != port && port.isOpen() && Ports.hasName(portName, port);
	}
	
	private void connect() {
		if(null == port) log.info("Looking up and opening port for first time");
		else if(!port.isOpen()) log.info("Port is not open, looking up and opening it again");
		else if(!Ports.hasName(portName, port)) log.info("Port name has changed, looking up and opening appropriate port");
		
		if(null != port) port.removeDataListener();
		port = Ports.findByName(portName);
		if(null != port) {
			port.setBaudRate(19200);
			port.setNumDataBits(8);
			port.setNumStopBits(1);
			port.setParity(SerialPort.NO_PARITY);
			port.openPort();
			port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 1000);
			port.addDataListener(this);
			log.info("Started listening on port " + Ports.toString(port));
		} else {
			log.error("Could not find any port for name " + portName);
		}
	}

	@Override
	public int getListeningEvents() {
		return SerialPort.LISTENING_EVENT_DATA_RECEIVED | SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
	}

	@Override
	public void serialEvent(SerialPortEvent event) {
		final byte[] response = event.getReceivedData();
		log.info("Received data from port : " + Strings.toHexString(response));
		if(33 > response.length) {
			log.error("Response too short, expected 33 bytes, got " + response.length);
		} else {
			final Set<StationStateChange> states = new HashSet<>();
			for(int index = 4; index < 33; index+=2) {
				for(BreakdownType type : BreakdownType.values()) {
					states.add(resolve(index - 3, response[index], type));
				}
			}
			try {
				stationStateChangeService.saveAll(states);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private StationStateChange resolve(long stationId, byte state, BreakdownType type) {
		final StationStateChange breakdown = new StationStateChange();
		breakdown.setType(type);
		breakdown.setStationId(stationId);
		breakdown.setTimestamp(new Date());
		breakdown.setState(resolve(state, type));
		return breakdown;
	}
	
	private StationState resolve(byte state, BreakdownType type) {
		return 0 < (state & ((byte)Math.pow(2, type.ordinal()))) ?
				StationState.DOWN : StationState.UP;
	}
	
}
