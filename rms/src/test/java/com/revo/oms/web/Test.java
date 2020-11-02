package com.revo.oms.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revo.rms.model.RequisitionStatus;
import com.revo.rms.model.dto.RequisitionDTO;

import lombok.SneakyThrows;

public class Test {
	
	@SneakyThrows
	public static void send2(RequisitionDTO dto) {
		final int port = 80;
		final String path = "/requisitions";
		final ObjectMapper objectMapper = new ObjectMapper();
		final String content = objectMapper.writeValueAsString(dto);
		final InetAddress address = InetAddress.getByName("localhost");
		final StringBuilder builder = new StringBuilder();
		builder.append("PATCH ").append(path).append(" HTTP/1.1").append("\r\n");
		builder.append("Host: ").append("localhostxxxx").append("\r\n");
		builder.append("Content-Type: ").append("application/json").append("\r\n");
		builder.append("Content-Length: ").append(String.valueOf(content.length())).append("\r\n");
		builder.append("\r\n");
		builder.append(content);
		builder.append("\r\n");
		
		System.out.println(builder.toString());
		
		final Socket socket = new Socket(address, port);
		socket.getOutputStream().write(builder.toString().getBytes());
		socket.getOutputStream().flush();
        
        final BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            System.out.println(line);
        }
        rd.close();
        socket.close();
	}
	

	public static void main(String[] args) {
		final AtomicLong index = new AtomicLong(9);
		final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
		
		final Runnable createRunnable = new Runnable() {
			@Override
			public void run() {
				final Long id = index.getAndIncrement();
				final RequisitionDTO dto = new RequisitionDTO();
				dto.setPartId(id);
				dto.setStationId(1l);
				dto.setQuantity(id);
				final Long requisitionId = post(dto);
				if(id < 19) executor.schedule(this, 3, TimeUnit.SECONDS);
				executor.schedule(() -> {
					dto.setId(requisitionId);
					dto.setStatus(RequisitionStatus.CLOSED);
					patch(dto);
				}, id - 3, TimeUnit.SECONDS);
			}
		};
		executor.schedule(createRunnable, 3, TimeUnit.SECONDS);
	}
	
	private static Long post(final RequisitionDTO dto) {
		final String url = "http://localhost/requisitions";
		final RestTemplate template = new RestTemplate();
		return template.postForEntity(url, dto, Long.class).getBody();
	}
	
	private static void patch(final RequisitionDTO dto) {
		final String url = "http://localhost/requisitions";
		final RestTemplate template = new RestTemplate();
		template.put(url, dto);
	}
	
	
}
