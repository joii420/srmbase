package com.step.quartz.runtime.listener;

import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerListener;

import java.util.logging.Logger;

public class MyTriggerListener implements TriggerListener {

    @Override
    public String getName() {
        String name = this.getClass().getSimpleName();
        //log.info("当前JobListener名称为"+name);
        return name;
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext jobExecutionContext) {
        String name = trigger.getKey().getName();
        //log.info("当前Job名称为" + name + ",JobDetail将要被执行");
    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext jobExecutionContext) {
        boolean temp = false;
        String name = trigger.getKey().getName();
        //log.info(name + " | Job将要被执行由scheduler调用这个方法，Trigger Listener给了一个选择去否决job的执行为" + temp);
        return temp;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {
        String name = trigger.getKey().getName();
        //log.info(name + " | scheduler调用这个方法是在Trigger错过触发时候调用该方法");
    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext jobExecutionContext, Trigger.CompletedExecutionInstruction completedExecutionInstruction) {
        String name = trigger.getKey().getName();
        //log.info(name + " | Trigger被触发并完成了job的执行，scheduler调用这个方法");
    }
}
