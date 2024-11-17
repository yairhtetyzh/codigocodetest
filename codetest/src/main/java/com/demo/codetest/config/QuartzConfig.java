package com.demo.codetest.config;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.demo.codetest.jobs.WaitingListJob;

@Configuration
public class QuartzConfig {

	@Bean
    public JobDetail waitingListJobDetail() {
        return JobBuilder.newJob(WaitingListJob.class)
                .withIdentity("waitingListJob")
                .storeDurably()
                .build();
    }
	
	@Bean
    public Trigger waitingListJobTrigger() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInMinutes(1) // Run every hour
                .repeatForever();

        return TriggerBuilder.newTrigger()
                .forJob(waitingListJobDetail())
                .withIdentity("waitingListJobTrigger")
                .withSchedule(scheduleBuilder)
                .build();
    }
}
