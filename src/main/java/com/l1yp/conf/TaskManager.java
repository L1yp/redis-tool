package com.l1yp.conf;

import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @Author Lyp
 * @Date 2020-07-06
 * @Email l1yp@qq.com
 */
@Component
public class TaskManager {

    @Resource
    private ThreadPoolTaskScheduler scheduler;

    private final static Map<String, ScheduledFuture<?>> tasks = new ConcurrentHashMap<>();

    public void startCronTask(String id, String expr, Runnable task){
        CronTrigger trigger = new CronTrigger(expr);
        ScheduledFuture<?> future = scheduler.schedule(task, trigger);
        tasks.put(id, future);
    }

    public void startTask(String id, Trigger trigger, Runnable task){
        ScheduledFuture<?> future = scheduler.schedule(task, trigger);
        tasks.put(id, future);
    }

    public void startTask(String id, long period, Runnable task){
        startTask(id, 0, period, TimeUnit.MILLISECONDS, task);
    }

    public void startTask(String id, long initialDelay, long period, TimeUnit unit, Runnable task){
        PeriodicTrigger trigger = new PeriodicTrigger(period, unit);
        trigger.setInitialDelay(initialDelay);
        ScheduledFuture<?> future = scheduler.schedule(task, trigger);
        tasks.put(id, future);
    }

    public void stop(String id){
        ScheduledFuture<?> scheduledFuture = tasks.remove(id);
        if (scheduledFuture != null){
            scheduledFuture.cancel(true);
        }
    }

    public void stopAll(){
        for (Entry<String, ScheduledFuture<?>> entry : tasks.entrySet()) {
            entry.getValue().cancel(true);
        }
        tasks.clear();
    }



}
