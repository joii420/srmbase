package com.step.quartz.runtime.listener;


import org.quartz.*;

import java.util.logging.Logger;

public class MySchedulerListener implements SchedulerListener {
    @Override
    public void jobScheduled(Trigger trigger) {
        //log.info(trigger.getKey().getName() + " | 用于部署Job Detail时调用");
    }

    @Override
    public void jobUnscheduled(TriggerKey triggerKey) {
      //  log.info(triggerKey.getName() + " | 用于卸载JobDetail时调用");
    }

    @Override
    public void triggerFinalized(Trigger trigger) {
        //log.info(trigger.getKey().getName() + " | 当一个Trigger来到了再也不会触发的状态时调用这个方法。除非这个Job已设置成了持久性，否则它就会中scheduler中移除");
    }

    @Override
    public void triggerPaused(TriggerKey triggerKey) {
       // log.info(triggerKey.getName() + " | scheduler调用这个方法时发生在一个Trigger被暂停时调用。");
    }

    @Override
    public void triggersPaused(String triggerGroup) {
       // log.info(triggerGroup + " | scheduler调用这个方法时发生在一个Trigger组被暂停时调用。");
    }

    @Override
    public void triggerResumed(TriggerKey triggerKey) {
        //log.info(triggerKey.getName() + " | scheduler调用这个方法发生在为一个Trigger从暂停中恢复时");
    }

    @Override
    public void triggersResumed(String triggerGroup) {
       // log.info(triggerGroup + " | scheduler调用这个方法发生在为一个Trigger组从暂停中恢复时");
    }

    @Override
    public void jobAdded(JobDetail jobDetail) {
       // log.info("添加任务时被调用");
    }

    @Override
    public void jobDeleted(JobKey jobKey) {
       // log.info("删除任务时被调用");
    }

    @Override
    public void jobPaused(JobKey jobKey) {
       // log.info("暂停任务时被调");
    }

    @Override
    public void jobsPaused(String jobGroup) {
        //log.info("暂停任务组时被调用");
    }

    @Override
    public void jobResumed(JobKey jobKey) {
        //log.info("恢复任务时被调用");
    }

    @Override
    public void jobsResumed(String jobGroup) {
       // log.info("恢复任务组时被调用");
    }

    @Override
    public void schedulerError(String msg, SchedulerException cause) {
        //log.info("在scheduler的政策运行期间发生严重错误时被调用");
    }

    @Override
    public void schedulerInStandbyMode() {
       // log.info("在scheduler处于挂载状态时被调用");
    }

    @Override
    public void schedulerStarted() {
        //log.info("在scheduler开启时被调用");
    }

    @Override
    public void schedulerStarting() {
        //log.info("当调度程序启动时调用该方法");
    }

    @Override
    public void schedulerShutdown() {
        //log.info("当scheduler关闭时调用该方法");
    }

    @Override
    public void schedulerShuttingdown() {
        //log.info("当调度程序关闭时调用");
    }

    @Override
    public void schedulingDataCleared() {
       // log.info("当scheduler中的数据被清除时，调用该方法");
    }
}
