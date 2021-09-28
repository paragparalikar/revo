package com.revosystems.cbms.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Strings {
	public static final String EMPTY = "";

	public boolean isBlank(String text) {
		return null == text || 0 == text.trim().length();
	}
	
	public boolean isNotBlank(String text) {
		return !isBlank(text);
	}
	
	@SneakyThrows
	public List<String> toLines(final byte[] data){
		final List<String> lines = new ArrayList<>();
		final InputStream inputStream = new ByteArrayInputStream(data);
		final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		String line = null;
		while(null != (line = bufferedReader.readLine())) {
			lines.add(line);
		}
		return lines;
	}
	
	private static final char[] hex = "0123456789abcdef".toCharArray();
	
	public static String toHexString(byte[] bytes) {
        if (null == bytes) {
            return null;
        }

        StringBuilder sb = new StringBuilder(bytes.length << 1);

        for (byte aByte : bytes) {
            sb.append(hex[(aByte & 0xf0) >> 4])
                    .append(hex[(aByte & 0x0f)])
            ;
        }

        return sb.toString();
    }
	
}
