package com.goodeelms.listener;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.quartz.SchedulerException;

import com.goodeelms.dto.AcademicCalendarDTO;
import com.goodeelms.scheduler.EndCartJobScheduler;
import com.goodeelms.scheduler.EndEnrollmentJobScheduler;
import com.goodeelms.scheduler.EndSemesterScheduler;
import com.goodeelms.scheduler.QuartzScheduleManager;
import com.goodeelms.scheduler.StartLectureJobScheduler;
import com.goodeelms.service.AcademicCalendarService;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.Getter;
import lombok.Setter;

/**
 * Application Lifecycle Listener implementation class LMSScheduleListener
 *
 */
@WebListener
public class LMSScheduleListener implements ServletContextListener {

	@Getter
	private static final ZoneId ZONE_ID = ZoneId.of("Asia/Seoul");
	
	@Getter @Setter
	private static Map<String, ZonedDateTime> eventTimeMap = new HashMap<String, ZonedDateTime>();
	
    public LMSScheduleListener() {
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent sce)  { 
    	
    	int year = LocalDateTime.now().getYear();
    	
    	// DB조회
    	List<AcademicCalendarDTO> list = AcademicCalendarService.getInstance().getCalendarAtYear(year);
    	if(list ==  null || list.size() == 0) {
    		System.out.println("DB 조회 실패 또는 데이터 소실");
    		return;
    	}
    	// year 년도 학사일정 조회해서 이름이랑 시간 매핑
    	Map<String, ZonedDateTime> eventTimeMap = list.stream().collect(Collectors.toMap(AcademicCalendarDTO::getAcademicEventName, AcademicCalendarDTO::getEventZoneDateTime));
    	
    	// 서버 시작 시 실행
        try {
            System.out.println("--- 스케줄러 초기화 시작 ---");
            QuartzScheduleManager.init(); // QuartzManager 가동
            
            // 객체가 아닌, 클래스를 전달해야함
            // 1학기 장바구니 종료 시 작업
            QuartzScheduleManager.addJob(EndCartJobScheduler.class, "FirstSemesterCartCommitEvent", eventTimeMap.get("student_first_lecture_cart_end"));
            
            // 1학기 수강신청 기간 종료 시 작업
            QuartzScheduleManager.addJob(EndEnrollmentJobScheduler.class, "FirstEnrollmentCommitEvent", eventTimeMap.get("student_first_enrollment_end"));  
            
            // 1학기 개강 작업
            QuartzScheduleManager.addJobWithTimeSemester(StartLectureJobScheduler.class, "FirstSemesterStart", year, 1, eventTimeMap.get("ac_open_first_semester"));
            
            // 1학기 종강 작업
            QuartzScheduleManager.addJobWithTimeSemester(EndSemesterScheduler.class, "FirstSemesterEnd", year, 1, eventTimeMap.get("ac_close_first_semester"));
            
            // 2학기 장바구니 종료 시 작업
            QuartzScheduleManager.addJob(EndCartJobScheduler.class, "SecondSemesterCartCommitEvent", eventTimeMap.get("student_second_lecture_cart_end"));
            
            // 2학기 수강신청 기간 종료 시 작업
            QuartzScheduleManager.addJob(EndEnrollmentJobScheduler.class, "SecondEnrollmentCommitEvent", eventTimeMap.get("student_second_enrollment_end"));  
            
            // 2학기 개강 작업
            QuartzScheduleManager.addJobWithTimeSemester(StartLectureJobScheduler.class, "SecondSemesterStart", year, 2, eventTimeMap.get("ac_open_second_semester"));
            
            // 2학기 종강 작업
            QuartzScheduleManager.addJobWithTimeSemester(EndSemesterScheduler.class, "SecondSemesterEnd", year, 2, eventTimeMap.get("ac_close_second_semester"));
            
            QuartzScheduleManager.printSchedulerStatus();
        } catch (SchedulerException e) {
            e.printStackTrace();
            contextDestroyed(sce);
        }

    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent sce)  { 
    	// 서버 종료 시 실행 (메모리 누수 방지)
        try {
            System.out.println("--- 스케줄러 종료 시작 ---");
            QuartzScheduleManager.shutdown(); // QuartzManager 종료 메서드 추가 필요
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
	
}
