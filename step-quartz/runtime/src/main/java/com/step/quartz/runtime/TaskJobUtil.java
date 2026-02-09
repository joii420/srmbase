package com.step.quartz.runtime;


import com.step.quartz.runtime.exception.QuartzException;
import com.step.quartz.runtime.model.JobParam;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


@ApplicationScoped
public class TaskJobUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskJobUtil.class.getName());
    public static final String TASK_JOB_GROUP = "Task#JobsGroup"; //任务组名
    public static final String TASK_TRIGGERS_GROUP = "Task#TriggersGroup";  //触发器组名
    public static final String TASK_ID_DATA_KEY = "taskId";

    @Inject
    Scheduler scheduler;

    public TaskJobUtil() {
    }


    public void addJob(JobParam jobParam) {
        addJob(jobParam.getName(), jobParam.getJobClass(), jobParam.getCron(), jobParam.getParam());
    }

    /**
     * 添加任务
     *
     * @param jobName
     * @param jobClazz
     * @param cron
     * @param param
     */
    public void addJob(String jobName, Class<? extends Job> jobClazz, String cron, Map<String, String> param) {
        try {
            param = Optional.ofNullable(param).orElse(new HashMap<>(0));
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, TASK_JOB_GROUP);
            Trigger trigger = scheduler.getTrigger(triggerKey);
            //CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
            if (scheduler.checkExists(triggerKey)) {
                LOGGER.info("触发器存在");
                //trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule()
            } else {
                //停止触发器
                scheduler.pauseTrigger(triggerKey);
                //移除触发器
                scheduler.unscheduleJob(triggerKey);
                //删除任务
                scheduler.deleteJob(JobKey.jobKey(jobName, TASK_JOB_GROUP));
                JobDetail jobDetail = JobBuilder
                        .newJob(jobClazz)
                        .withIdentity(jobName, TASK_JOB_GROUP)
                        .withDescription(param.get("DESCRIPTION"))
                        .build();
                JobDataMap jobDataMap = jobDetail.getJobDataMap();
                if (param.size() != 0) {
                    for (String s : param.keySet()) {
                        jobDataMap.put(s, param.get(s));
                    }
                }
                //触发器
                CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                        .withIdentity(jobName, TASK_TRIGGERS_GROUP)
                        .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                        .build();
                scheduler.scheduleJob(jobDetail, cronTrigger);
                if (!scheduler.isShutdown()) {
                    //绑定监听
                    //1全局绑定 所有job在被调度的时候都会被监听
                    //scheduler.getListenerManager().addJobListener(new MyJobListener(), EverythingMatcher.allJobs());
                    //2局部监听 用来监听指定的job
                    //scheduler.getListenerManager().addJobListener(new MyJobListener(), KeyMatcher.keyEquals(JobKey.jobKey("job1","group1")));
                    scheduler.start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new QuartzException(e.getMessage());
        }
    }

    /**
     * 删除定时任务
     *
     * @param jobName
     */
    public void removeJob(String jobName) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, TASK_TRIGGERS_GROUP);
            //停止触发器
            scheduler.pauseTrigger(triggerKey);
            //移除触发器
            scheduler.unscheduleJob(triggerKey);
            //删除任务
            scheduler.deleteJob(JobKey.jobKey(jobName, TASK_JOB_GROUP));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动任务
     *
     * @param
     */
    public void startJob() {
        try {
            scheduler.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除任务
     *
     * @param
     */
    public void shutdownJobs() {
        try {
            if (!scheduler.isShutdown()) {
                scheduler.shutdown();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取所有任务
     *
     * @param
     */
    public List<String> getAllJob() {
        List<String> list = new ArrayList<>();
        try {

            Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.anyGroup());
            for (JobKey j : jobKeys) {
                JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(j.getName(), TASK_JOB_GROUP));
                Trigger trigger = scheduler.getTrigger(TriggerKey.triggerKey(j.getName(), TASK_TRIGGERS_GROUP));
                Trigger.TriggerState triggerState = scheduler.getTriggerState(TriggerKey.triggerKey(j.getName(), TASK_TRIGGERS_GROUP));
                list.add(j.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
