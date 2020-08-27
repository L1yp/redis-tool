package com.l1yp.controller;

import com.l1yp.conf.ScheduleConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * @Author Lyp
 * @Date 2020-07-06
 * @Email l1yp@qq.com
 */
@RestController
@Slf4j
public class TaskController {

    private static class TaskRunnable implements Runnable {

        private String taskId;

        public TaskRunnable(String taskId){
            this.taskId = taskId;
        }

        @Override
        public void run() {
            log.info("run task [{}], currentTime {}", taskId, (System.currentTimeMillis() / 1000));
        }
    }

    @Resource
    private ThreadPoolTaskScheduler scheduler;

    private static Map<String, ScheduledFuture<?>> tasks = new ConcurrentHashMap<>();

    @RequestMapping("/test")
    public String test(){
        return "OK";
    }

    @RequestMapping("/start")
    public String startTask(String taskId){
        log.info("start task {}", taskId);
        ScheduledFuture<?> scheduledFuture = scheduler.scheduleAtFixedRate(new TaskRunnable(taskId), 3000);
        tasks.put(taskId, scheduledFuture);
        return "OK";
    }

    @Async
    @RequestMapping("/stop")
    public String stopTask(String taskId){
        log.info("stop task {}", taskId);
        ScheduledFuture<?> scheduledFuture = tasks.remove(taskId);
        if (scheduledFuture != null){
            scheduledFuture.cancel(true);
        }
        return "OK";
    }

    @RequestMapping("/allTask")
    public String allTask(){

        for (String taskId : tasks.keySet()) {
            log.info("task {}", taskId);
        }
        return "OK";
    }

}
