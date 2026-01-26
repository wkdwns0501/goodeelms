package com.goodeelms.util;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import org.quartz.SchedulerException;

import com.goodeelms.listener.LMSScheduleListener;
import com.goodeelms.scheduler.QuartzScheduleManager;

import lombok.Getter;


public class StaticUtils {
	
	@Getter
	private static volatile ZonedDateTime settedTime = ZonedDateTime.now(LMSScheduleListener.getZONE_ID());
	private StaticUtils() {
		
	}
	
	public static boolean isBetweenTime(ZonedDateTime now, ZonedDateTime startTime, ZonedDateTime endTime) {
		return now.isAfter(startTime) && now.isBefore(endTime);
	} 
	
	public static void setSettedTime(LocalDate dateTime) {
		ZonedDateTime beforeTime = settedTime;
		ZonedDateTime afterTime = dateTime.atTime(12,0).atZone(LMSScheduleListener.getZONE_ID());
		settedTime = afterTime;
		try {
			QuartzScheduleManager.catchUpIfNeeded(beforeTime, afterTime);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
}
