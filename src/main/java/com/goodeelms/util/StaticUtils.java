package com.goodeelms.util;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import com.goodeelms.listener.LMSScheduleListener;

import lombok.Getter;
import lombok.Setter;


public class StaticUtils {
   
   @Getter
   private static volatile ZonedDateTime settedTime = ZonedDateTime.now(LMSScheduleListener.getZONE_ID());
   private StaticUtils() {
      
   }
   
   public static boolean isBetweenTime(ZonedDateTime now, ZonedDateTime startTime, ZonedDateTime endTime) {
      System.out.println(now);
      System.out.println(startTime);
      System.out.println(endTime);
      return now.isAfter(startTime) && now.isBefore(endTime);
   }
   
   public static void setSettedTime(LocalDateTime dateTime) {
      settedTime = dateTime.atZone(LMSScheduleListener.getZONE_ID());
   }
}
