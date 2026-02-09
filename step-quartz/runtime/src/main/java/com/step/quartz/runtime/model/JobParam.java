package com.step.quartz.runtime.model;

import com.step.quartz.runtime.exception.QuartzException;
import com.step.logger.LOGGER;
import org.quartz.CronExpression;
import org.quartz.Job;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class JobParam {

    private String name;
    private String cron;
    /**
     * 是否串行执行
     */
    private Class<? extends Job> jobClass;

    private final Map<String, String> param;

    public JobParam() {
        this.param = new HashMap<>(0);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public Class<? extends Job> getJobClass() {
        return jobClass;
    }

    public void setJobClass(Class<? extends Job> jobClass) {
        this.jobClass = jobClass;
    }


    public Map<String, String> getParam() {
        return param;
    }

    public void setExecTime(long execTime) {
        this.param.put("EXEC_TIME", "" + execTime);
    }

    public Map<String, String> addParam(String key, String value) {
        this.param.put(key, value);
        return this.param;
    }

    public Map<String, String> removeParam(String key) {
        this.param.remove(key);
        return this.param;
    }

    public static void isValidCronExpression(String expression) {
//        String cronRegex = "^\\s*([0-9]\\d*|\\*)\\s+([0-9]\\d*|\\*)\\s+([0-9]\\d*|\\*)\\s+([0-9]\\d*|\\*)\\s+([0-9]\\d*|\\*)\\s+([0-9]\\d*|\\*)\\s*$";
//        return Pattern.matches(cronRegex, expression);
        try {
            new CronExpression(expression);
        } catch (ParseException e) {
            LOGGER.error("无效的CRON表达式 : {} ", expression, e);
            throw new QuartzException("Invalid cron expression");
        }
    }
}
