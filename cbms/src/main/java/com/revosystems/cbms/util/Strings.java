package com.revosystems.cbms.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Strings {

	public boolean isBlank(String text) {
		return null == text || 0 == text.trim().length();
	}
	
	public boolean isNotBlank(String text) {
		return !isBlank(text);
	}
	
}
