package com.goodeelms.service;

import java.time.ZonedDateTime;
import java.util.Map;

import com.goodeelms.listener.LMSScheduleListener;
import com.goodeelms.util.StaticUtils;

public class AccessPeriodService {
	    // 테스트를 위해 시간을 주입받을 수 있는 구조 유지
	    public boolean isAccessPeriod(ZonedDateTime now) {
	        // 1. 리스너가 관리하는 메모리 맵(Map<String, ZonedDateTime>)을 가져옵니다.
	        Map<String, ZonedDateTime> eventMap = LMSScheduleListener.getEventTimeMap();
	        
	        if (eventMap == null || eventMap.isEmpty()) {
	            System.out.println("WARN: 장학 관리 일정 맵이 비어있습니다.");
	            return false;
	        }
	        ZonedDateTime startTime = null;
	        ZonedDateTime endTime = null;
	        // 2. 맵에서 필요한 키값으로 즉시 꺼냅니다. (반복문 필요 없음!)
	        int month = now.getMonthValue();
	        if(month >= 1 && month < 7) {
	        startTime = eventMap.get("ac_first_lecture_evaluation_start");
	        endTime = eventMap.get("ac_first_lecture_evaluation_end");
	        } else if (month >= 7) {
	        startTime = eventMap.get("ac_second_lecture_evaluation_start");
		    endTime = eventMap.get("ac_second_lecture_evaluation_end");	
	        }
	        // 3. 기간 비교
	        if (startTime != null && endTime != null) {
	            return StaticUtils.isBetweenTime(now, startTime, endTime);
	        }

	        return false;
	    }
}