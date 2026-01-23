package com.goodeelms.util;

import java.time.ZonedDateTime;

public class StaticUtils {

	private StaticUtils() {
		
	}
	
	public static boolean isBetweenTime(ZonedDateTime now, ZonedDateTime startTime, ZonedDateTime endTime) {
		System.out.println(now);
		System.out.println(startTime);
		System.out.println(endTime);
		return now.isAfter(startTime) && now.isBefore(endTime);
	}
}
