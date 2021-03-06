package com.usian.config;

import com.usian.factory.MyAdaptableJobFactory;
import com.usian.quartz.OrderQuartz;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class QuartzConfig {

    /**
     * 创建job对象：做什么事
     * @return
     */
    @Bean
    public JobDetailFactoryBean getJobDetailFactoryBean(){
        JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
        jobDetailFactoryBean.setJobClass(OrderQuartz.class);
        return jobDetailFactoryBean;
    }

    /**
     * 创建trigger对象：什么时间
     */
    @Bean
    public CronTriggerFactoryBean getCronTriggerFactoryBean(JobDetailFactoryBean jobDetailFactoryBean){
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        cronTriggerFactoryBean.setJobDetail(jobDetailFactoryBean.getObject());
        cronTriggerFactoryBean.setCronExpression("*/5 * * * * ?");
        return cronTriggerFactoryBean;
    }

    /**
     * 创建scheduler：什么时间做什么事
     */
    @Bean
    public SchedulerFactoryBean getSchedulerFactoryBean(CronTriggerFactoryBean cronTriggerFactoryBean,
                                                        MyAdaptableJobFactory myAdaptableJobFactory){
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setTriggers(cronTriggerFactoryBean.getObject());
        //将OrderQuartz实例化，并添加到spring容器
        schedulerFactoryBean.setJobFactory(myAdaptableJobFactory);
        return schedulerFactoryBean;
    }
}
