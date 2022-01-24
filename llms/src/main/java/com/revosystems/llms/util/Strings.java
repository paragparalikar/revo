package com.revosystems.llms.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Strings {
	public static final String EMPTY = "";

	public static boolean isBlank(String text) {
		return null == text || 0 == text.trim().length();
	}
	
	public static boolean isNotBlank(String text) {
		return !isBlank(text);
	}
	
	public static List<String> toLines(final byte[] data) {
		try {
			final List<String> lines = new ArrayList<>();
			final InputStream inputStream = new ByteArrayInputStream(data);
			final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String line = null;
			while(null != (line = bufferedReader.readLine())) {
				lines.add(line);
			}
			return lines;
		} catch(RuntimeException re) {
			throw re;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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
