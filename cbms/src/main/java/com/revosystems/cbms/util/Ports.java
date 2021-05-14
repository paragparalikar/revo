package com.revosystems.cbms.util;

import java.util.stream.Stream;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

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
	
	public static void main(String[] args) throws InterruptedException {
		final byte[] request = new byte[] {
				(byte) Integer.parseInt("01", 16),
				(byte) Integer.parseInt("03", 16),
				(byte) Integer.parseInt("00", 16),
				(byte) Integer.parseInt("00", 16),
				(byte) Integer.parseInt("00", 16),
				(byte) Integer.parseInt("08", 16),
				(byte) Integer.parseInt("44", 16),
				(byte) Integer.parseInt("0C", 16)}; 
		
		final byte[] request1 = new byte[] {1,3,0,0,0,8,68,12};
		
		SerialPort port = Ports.findByName("CH340");
		port.setBaudRate(19200);
		port.setNumDataBits(8);
		port.setNumStopBits(1);
		port.setParity(SerialPort.NO_PARITY);
		port.openPort();
		port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 1000);
		port.addDataListener(new SerialPortDataListener() {

			@Override
			public int getListeningEvents() {
				return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
			}

			@Override
			public void serialEvent(SerialPortEvent event) {
				final byte[] response = event.getReceivedData();
				for(byte value : response) {
					final int intValue = value & 0xFF;
					System.out.print("["+intValue+"]");
				}
				System.out.println();
			}
			
		});
		
		for(int index = 0; index < 10; index++) {
			port.writeBytes(request1, 8);
			while(port.bytesAwaitingWrite() > 0) {
				System.out.println("waiting to write " + port.bytesAwaitingWrite());
			}
			Thread.sleep(1000l);
		}
	}
	
}
