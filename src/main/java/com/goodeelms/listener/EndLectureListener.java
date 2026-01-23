package com.goodeelms.listener;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.goodeelms.service.LectureCartService;
import com.goodeelms.service.LectureService;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.Getter;

@WebListener
public class EndLectureListener implements ServletContextListener {

	// 스케줄러 설정
	private ScheduledExecutorService scheduler;
	private ScheduledFuture<?> endFirstSemesterFuture; 
	private ScheduledFuture<?> secondFirstSemesterFuture; 
	
	// 연도 호출
	private static final int CURRENT_YEAR = LocalDateTime.now().getYear();
	// 스케줄 시간 설정
	@Getter
	private static final LocalDateTime END_FIRST_SEMESTER = LocalDateTime.of(CURRENT_YEAR, 6, 30, 0, 0);
	@Getter
	private static final LocalDateTime END_SECOND_SEMESTER = LocalDateTime.of(CURRENT_YEAR, 12, 31, 0, 0);
	
    public EndLectureListener() {
    }

    public void contextInitialized(ServletContextEvent sce)  { 
    	
    	// 스케쥴러 스레드 부여
    	scheduler = Executors.newSingleThreadScheduledExecutor(r ->{
    		Thread t = new Thread(r);
    		t.setName("enroll-total-scheduler");
    		t.setDaemon(true);
    		return t;
    	});
    	
    	// 6월 말 1학기 강의 종강
    	scheduleIfNeeded(END_FIRST_SEMESTER,
    			() -> LectureService.getInstance().updateLectureStatusToEnd(CURRENT_YEAR, 1),
    			f -> endFirstSemesterFuture = f);
    	
    	// 12월 말 2학기 강의 종강
    	scheduleIfNeeded(END_SECOND_SEMESTER,
    			() -> LectureService.getInstance().updateLectureStatusToEnd(CURRENT_YEAR, 2),
    			f -> secondFirstSemesterFuture = f);
    }

    public void contextDestroyed(ServletContextEvent sce)  {
    	if (endFirstSemesterFuture != null) endFirstSemesterFuture.cancel(false);
    	if (secondFirstSemesterFuture != null) secondFirstSemesterFuture.cancel(false);
        if (scheduler != null) scheduler.shutdownNow();
    }
    
    private void scheduleIfNeeded(LocalDateTime targetTime, Runnable task, Consumer<ScheduledFuture<?>> futureConsumer) {
    
    	Instant now = Instant.now();
    	Instant target = targetTime.atZone(CloseCartListener.getZONE_ID()).toInstant();
    	
    	long delayMs = ChronoUnit.MILLIS.between(now, target);
    	// 이미 지났으면 확정 로직 즉시 실행
    	if(delayMs < 0)	delayMs = 0;
    	
    	// 실행 조건 부여 -> 실행 객체, 지연 시간, 시간 단위
    	ScheduledFuture<?> future = scheduler.schedule(new SafeJobWrapper(task), delayMs, TimeUnit.MILLISECONDS);
    	
    	futureConsumer.accept(future);
    }
    
    static class SafeJobWrapper implements Runnable {
        private final Runnable delegate;
        SafeJobWrapper(Runnable delegate) { this.delegate = delegate; }

        @Override
        public void run() {
            try {
                delegate.run();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

}
