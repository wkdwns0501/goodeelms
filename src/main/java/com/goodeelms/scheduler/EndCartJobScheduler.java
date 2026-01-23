package com.goodeelms.scheduler;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.quartz.DateBuilder;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import com.goodeelms.service.LectureCartService;

public class EndCartJobScheduler implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("장바구니 마감 작업 실행 시간: " + LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")));
		
        try {
            // 로직 수행 (예: DB 상태 업데이트)
            boolean isSuccess = new LectureCartService().closeCartStatus();
            
            if (!isSuccess) {
                throw new Exception("비즈니스 로직 실패");
            }
        } catch (Exception e) {
            // [결과에 따른 재실행 로직]
            System.err.println("실패: 10초 후 재시도합니다.");
            
            // 10초 뒤에 실행될 새로운 트리거 생성 및 교체
            Trigger retryTrigger = TriggerBuilder.newTrigger()
                .startAt(DateBuilder.futureDate(10, DateBuilder.IntervalUnit.SECOND))
                .build();
            
            try {
                context.getScheduler().rescheduleJob(context.getTrigger().getKey(), retryTrigger);
            } catch (SchedulerException se) {
                se.printStackTrace();
            }
        }
	}

}
