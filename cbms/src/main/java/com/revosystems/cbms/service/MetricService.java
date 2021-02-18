package com.revosystems.cbms.service;

import org.springframework.context.Lifecycle;
import org.springframework.stereotype.Service;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.revosystems.cbms.repository.MetricRepository;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@Service
@RequiredArgsConstructor
public class MetricService implements Lifecycle {

	@NonNull
	@Delegate
	private final MetricRepository metricRepository;
	
	@Getter private boolean running;

	@Override
	public void start() {
		
	}

	@Override
	public void stop() {
		
	}
	
	
	public static void main(String[] args) {
		SerialPort comPort = SerialPort.getCommPorts()[0];
		comPort.openPort();
		comPort.addDataListener(new SerialPortDataListener() {
		   @Override
		   public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_RECEIVED; }
		   @Override
		   public void serialEvent(SerialPortEvent event)
		   {
		      byte[] newData = event.getReceivedData();
		      System.out.println("Received data of size: " + newData.length);
		      for (int i = 0; i < newData.length; ++i)
		         System.out.print((char)newData[i]);
		      System.out.println("\n");
		   }
		});
	}
	
}
