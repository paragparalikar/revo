package com.revo.oms.web;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TestServer{
	
	
	public static void main(String[] args) throws Exception {
		ServerSocket serverSocket = new ServerSocket(80);
		final Socket socket = serverSocket.accept();
		
		final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String line;
		while((line = reader.readLine()).length() != 0) {
			System.out.println(line);
		}
		
		final char[] chars = new char[51];
		reader.read(chars);
		System.out.println(new String(chars));
		
		final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		writer.write("200 OK");
		writer.newLine();
		writer.flush();
		writer.close();
		
		socket.close();
		serverSocket.close();
	}
	
}
