package com.step.quartz.runtime.listener;

import io.quarkus.runtime.StartupEvent;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.matchers.EverythingMatcher;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

@ApplicationScoped
public class ListenerManger {
    @Inject
    Scheduler scheduler;
    void onStrat(@Observes StartupEvent event) throws SchedulerException {
        //1全局绑定 所有job在被调度的时候都会被监听
        scheduler.getListenerManager().addJobListener(new MyJobListener(), EverythingMatcher.allJobs());
        //scheduler.getListenerManager().addTriggerListener(new MyTriggerListener(), EverythingMatcher.allTriggers());
        scheduler.getListenerManager().addSchedulerListener(new MySchedulerListener());
        //2局部监听 用来监听指定的job
        //scheduler.getListenerManager().addJobListener(new MyJobListener(), KeyMatcher.keyEquals(JobKey.jobKey("job1","group1")));
    }
}
