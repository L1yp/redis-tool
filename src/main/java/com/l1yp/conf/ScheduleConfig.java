package com.l1yp.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @Author Lyp
 * @Date 2020-07-06
 * @Email l1yp@qq.com
 */
@Configuration
@EnableScheduling
public class ScheduleConfig {

    @Bean(name = ScheduledAnnotationBeanPostProcessor.DEFAULT_TASK_SCHEDULER_BEAN_NAME)
    public ThreadPoolTaskScheduler taskScheduler(){
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        // scheduler.schedule()

        return scheduler;
    }

}
