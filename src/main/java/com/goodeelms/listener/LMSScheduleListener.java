package com.goodeelms.listener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
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
import com.goodeelms.service.AcademicCalendarService;
import com.goodeelms.util.DBUtil;

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
    	eventTimeMap = list.stream().collect(Collectors.toMap(AcademicCalendarDTO::getAcademicEventName, AcademicCalendarDTO::getEventZoneDateTime));
    	
//    	firstGradeInsertStart = eventTimeMap.get("ac_first_grade_insert_start");
//    	firstGradeInsertEnd = eventTimeMap.get("ac_first_grade_insert_end");
//    	
//    	firstLectureEvaluationStart = eventTimeMap.get("ac_first_lecture_evaluation_start");
//    	firstLectureEvaluationEnd = eventTimeMap.get("ac_first_lecture_evaluation_end");
//    	
//    	firstScholarshipSelectStart = eventTimeMap.get("ac_first_scholarship_select_start");
//    	firstScholarshipSelectEnd = eventTimeMap.get("ac_first_scholarship_select_end");
//    	
//    	studentFirstLectureCartStart = eventTimeMap.get("student_first_lecture_cart_start");
//    	studentFirstLectureCartEnd = eventTimeMap.get("student_first_lecture_cart_end");
//    	
//    	studentFirstEnrollmentStart = eventTimeMap.get("student_first_enrollment_start");
//    	studentFirstEnrollmentEnd = eventTimeMap.get("student_first_enrollment_end");
//    	
//    	openFirstSemester = eventTimeMap.get("ac_open_first_semester");
//    	closeFirstSemester = eventTimeMap.get("ac_close_first_semester");
//    	
//    	firstGradeInsertStart = eventTimeMap.get("ac_first_grade_insert_start");
//    	firstGradeInsertEnd = eventTimeMap.get("ac_first_grade_insert_end");
//    	
//    	firstLectureEvaluationStart = eventTimeMap.get("ac_first_lecture_evaluation_start");
//    	firstLectureEvaluationEnd = eventTimeMap.get("ac_first_lecture_evaluation_end");
//    	
//    	firstScholarshipSelectStart = eventTimeMap.get("ac_first_scholarship_select_start");
//    	firstScholarshipSelectEnd = eventTimeMap.get("ac_first_scholarship_select_end");
//    	
//    	studentFirstLectureCartStart = eventTimeMap.get("student_first_lecture_cart_start");
//    	studentFirstLectureCartEnd = eventTimeMap.get("student_first_lecture_cart_end");
//    	
//    	studentFirstEnrollmentStart = eventTimeMap.get("student_first_enrollment_start");
//    	studentFirstEnrollmentEnd = eventTimeMap.get("student_first_enrollment_end");
//    	
//    	openFirstSemester = eventTimeMap.get("ac_open_first_semester");
//    	closeSecondSemester = eventTimeMap.get("ac_close_second_semester");
    	
    	
    	// 서버 시작 시 실행
        try {
            System.out.println("--- 스케줄러 초기화 시작 ---");
            QuartzScheduleManager.init(); // QuartzManager 가동
            
            // 객체가 아닌, 클래스를 전달해야함
            // 1학기 장바구니 종료 시 작업
            QuartzScheduleManager.addJobWithTimeSemester(EndCartJobScheduler.class, "CartCommitEvent", year, 1, eventTimeMap.get("student_first_lecture_cart_end"));
            
            // 수강신청 기간 종료 시 작업
            QuartzScheduleManager.addJobWithTimeSemester(EndEnrollmentJobScheduler.class, "EnrollmentCommitEvent", year, 1, eventTimeMap.get("student_first_enrollment_end"));  
            
            // 1학기 종강
            QuartzScheduleManager.addJobWithTimeSemester(EndSemesterScheduler.class, "FirstSemesterEnd", year, 1, eventTimeMap.get("ac_first_semester_end"));
            
            // 2학기 종강
            QuartzScheduleManager.addJobWithTimeSemester(EndSemesterScheduler.class, "SecondSemesterEnd", year, 2, eventTimeMap.get("ac_second_semester_end"));
            
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
