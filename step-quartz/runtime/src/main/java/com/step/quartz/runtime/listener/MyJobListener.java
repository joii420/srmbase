package com.step.quartz.runtime.listener;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MyJobListener implements JobListener {
    public static final Logger log = LoggerFactory.getLogger(MyJobListener.class);

    //    private static final Logger log = Logger.getLogger(MyJobListener.class.getName());
    @Override
    public String getName() {
        String name = this.getClass().getName();
        //log.info("当前JobListener名称为"+name);
        //System.out.println("当前JobListener名称为"+name);
        return name;
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext jobExecutionContext) {
//        JobDetail jobDetail = jobExecutionContext.getJobDetail();
//        JobDataMap jobDataMap = jobDetail.getJobDataMap();
//        String code = jobDataMap.getString("CODE");
//        String name = jobDataMap.getString("NAME");
//        log.info("定时任务 [ {} - {} ] 开始运行", code, name);
        //System.out.println("当前Job名称为"+name+",JobDetail将要被执行");
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext jobExecutionContext) {
        String name = jobExecutionContext.getJobDetail().getJobClass().getName();
        // log.info("当前Job名称为"+name+",JobDetail将要被执行，但被TriggerListener否决");
        //System.out.println("当前Job名称为"+name+",JobDetail将要被执行，但被TriggerListener否决");
    }

    @Override
    public void jobWasExecuted(JobExecutionContext jobExecutionContext, JobExecutionException e) {
//        JobDetail jobDetail = jobExecutionContext.getJobDetail();
//        JobDataMap jobDataMap = jobDetail.getJobDataMap();
//        String code = jobDataMap.getString("CODE");
//        String name = jobDataMap.getString("NAME");
//        log.info("定时任务 [ {} - {} ] 运行完成", code, name);
        // log.info("当前Job名称为"+name+",JobDetail执行完成");
        //System.out.println("当前Job名称为"+name+",JobDetail执行完成");
    }
}
