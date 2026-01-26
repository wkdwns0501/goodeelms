package com.goodeelms.scheduler;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

import com.goodeelms.listener.LMSScheduleListener;

public class QuartzScheduleManager {
	private static Scheduler scheduler;
	
	
	public static void init() throws SchedulerException {
		if(scheduler == null || scheduler.isShutdown()) {
			scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
		}
	}
	
	// 어디서든 이 메서드를 통해 새로운 작업을 등록하거나 변경함
    public static void addJob(Class<? extends Job> jobClass, String jobName, ZonedDateTime startTime) throws SchedulerException {
        JobDetail job = JobBuilder.newJob(jobClass)
                .withIdentity(jobName, "group1")
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(jobName + "Trigger", "group1")
                .startAt(startTime.toInstant())
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();

        // 동일한 이름의 Job이 있다면 삭제 후 재등록 (안전장치)
        if (scheduler.checkExists(job.getKey())) {
            scheduler.deleteJob(job.getKey());
        }
        scheduler.scheduleJob(job, trigger);
    }
    
    // 어디서든 이 메서드를 통해 새로운 작업을 등록하거나 변경함
    public static void addJobWithTimeSemester(Class<? extends Job> jobClass, String jobName, int year, int semester, ZonedDateTime startTime) throws SchedulerException {
    	// startTime이 null일 경우에 대한 방어 로직
        if (startTime == null) {
            System.err.println("경고: [" + jobName + "]의 startTime이 null입니다. 현재 시간으로 대체합니다.");
            startTime = ZonedDateTime.now(); // 혹은 return; 을 통해 등록을 취소
        }
    	
    	JobDetail job = JobBuilder.newJob(jobClass)
                .withIdentity(jobName, "group1")
                .usingJobData("year", year)
                .usingJobData("semester", semester)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(jobName + "Trigger", "group1")
                .startAt(startTime.toInstant())
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();
        
        // 동일한 이름의 Job이 있다면 삭제 후 재등록 (안전장치)
        if (scheduler.checkExists(job.getKey())) {
            scheduler.deleteJob(job.getKey());
        }
        scheduler.scheduleJob(job, trigger);
    }

    // 시간 변경 (Reschedule)
    public static void updateJobTime(String jobName, ZonedDateTime resetTime) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName + "Trigger", "group1");
        Trigger newTrigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .startAt(resetTime.toInstant())
                .build();
        
        scheduler.rescheduleJob(triggerKey, newTrigger);
    }
    
    public static void catchUpIfNeeded(ZonedDateTime beforeTime, ZonedDateTime afterTime) throws SchedulerException {
    	if(beforeTime == null || afterTime == null) return;
    	// 날짜가 앞으로 이동하거나 변동 없으면 미실행(복구가 안될걸?)
    	if(afterTime.isBefore(beforeTime) || afterTime.equals(beforeTime)) return;
    	
    	Map<String, ZonedDateTime> eventMap = LMSScheduleListener.getEventTimeMap();
    	// 동시성 검증
    	if (eventMap == null || eventMap.isEmpty()) return;
    	
    	System.out.println("시간 점프 트리거 확인");
    	for(Map.Entry<String, ZonedDateTime> entry : eventMap.entrySet()) {
    		String jobName = entry.getKey();
    		ZonedDateTime jobTime = entry.getValue() ;
    		if(jobTime == null) continue;
    		
    		boolean passed = beforeTime.isBefore(jobTime) && !jobTime.isAfter(afterTime);
    		if(!passed) continue;
    		
    		JobKey jobkey = JobKey.jobKey(jobName,"group1");
    		if(scheduler.checkExists(jobkey)) {
    			// 로그 기록 권장: "시간 변경으로 인한 누락 작업 강제 실행: [jobName]"
                System.out.println("[Catch-Up] Triggering missed job: " + jobName);
                scheduler.triggerJob(jobkey);
    		}
    	}
    }
	
	public static void shutdown() throws SchedulerException {
        if (scheduler != null && !scheduler.isShutdown()) {
            // true를 주면 현재 실행 중인 작업이 끝날 때까지 기다린 후 종료합니다.
            scheduler.shutdown(true);
        }
    }
	
	public static void printSchedulerStatus() throws SchedulerException {
        System.out.println("\n========== [Quartz Job Status Check] ==========");
        
        // 현재 스케줄러에 등록된 모든 Job 그룹 이름을 가져옴
        for (String groupName : scheduler.getJobGroupNames()) {
            for (org.quartz.JobKey jobKey : scheduler.getJobKeys(org.quartz.impl.matchers.GroupMatcher.jobGroupEquals(groupName))) {
                
                String jobName = jobKey.getName();
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                
                for (Trigger trigger : triggers) {
                    Trigger.TriggerState state = scheduler.getTriggerState(trigger.getKey());
                    ZonedDateTime nextFireTime = trigger.getNextFireTime() != null ? 
                        trigger.getNextFireTime().toInstant().atZone(LMSScheduleListener.getZONE_ID()) : null;
                    ZonedDateTime previousFireTime = trigger.getPreviousFireTime() != null ? 
                        trigger.getPreviousFireTime().toInstant().atZone(java.time.ZoneId.systemDefault()) : null;

                    System.out.println("Job Name      : " + jobName);
                    System.out.println("Trigger State : " + state); // NORMAL, PAUSED, COMPLETE, ERROR 등
                    System.out.println("Next Fire     : " + (nextFireTime != null ? nextFireTime : "None (Past/Finished)"));
                    System.out.println("Prev Fire     : " + (previousFireTime != null ? previousFireTime : "Never Executed"));
                    System.out.println("-----------------------------------------------");
                }
            }
        }
        System.out.println("===============================================\n");
    }
}
