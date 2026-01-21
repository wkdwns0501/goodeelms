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

import com.goodeelms.service.LectureCartService;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.Getter;

@WebListener
public class CloseCartListener implements ServletContextListener {

	// 스케줄러 설정
	private ScheduledExecutorService scheduler;
	private ScheduledFuture<?> future; 
	
	// 시간 설정
	@Getter
	private static final ZoneId ZONE_ID = ZoneId.of("Asia/Seoul");
	@Getter
	private static final long DURATION_AND_GAP = 7;
	@Getter
	private static final LocalDateTime START_CART_TIME = LocalDateTime.of(2026, 01, 20, 0, 0);
	@Getter
	private static final LocalDateTime END_CART_TIME = START_CART_TIME.plusDays(DURATION_AND_GAP);
	@Getter
	private static final LocalDateTime START_COM_TIME = END_CART_TIME.plusDays(DURATION_AND_GAP);
	@Getter
	private static final LocalDateTime END_COM_TIME = START_COM_TIME.plusDays(DURATION_AND_GAP);
	
    public CloseCartListener() {
    }

    public void contextInitialized(ServletContextEvent sce)  { 
    	// 현재 시간 로드
		ZonedDateTime nowTime = ZonedDateTime.now(ZONE_ID);
    	if(nowTime.isBefore(ZonedDateTime.of(START_CART_TIME, ZONE_ID))) {
    		System.out.println("Listener Said: 장바구니 시간 이전입니다. 리스너 가동 중단");
    		return;
    	}
    	scheduler = Executors.newSingleThreadScheduledExecutor(r ->{
    		Thread t = new Thread(r);
    		t.setName("close-cart-job");
    		t.setDaemon(true);
    		return t;
    	});
    	
    	Instant now = Instant.now();
    	Instant target = END_CART_TIME.atZone(ZONE_ID).toInstant();
    	
    	long delayMs = ChronoUnit.MILLIS.between(now, target);
    	
    	// 이미 지났으면 확정 로직 즉시 실행
    	if(delayMs < 0) delayMs = 0;
    	
    	// 실제 실행 로직
    	Runnable job = new SafeJobWrapper(() -> {
    		new LectureCartService().closeCartStatus();
    	});
    	
    	// 실행 조건 부여 -> 실행 객체, 지연 시간, 시간 단위
    	future = scheduler.schedule(job, delayMs, TimeUnit.MILLISECONDS);
    	
    }

    public void contextDestroyed(ServletContextEvent sce)  {
    	if (future != null) future.cancel(false);
        if (scheduler != null) scheduler.shutdownNow();
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
