package com.goodeelms.util;

public class ExistUtil {
	public static Boolean isNull(String str) {
		Boolean result = false;
		if(str == null || str.isBlank()) result = true;
		return result;
	}
}
