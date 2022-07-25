/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.bjornoya.schedule;

import org.joda.time.DateTime;
import org.quartz.CronScheduleBuilder;
import org.quartz.DateBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public abstract class ScheduledJob extends QuartzJobBean implements CronExpressionValidator, Comparable<ScheduledJob> {
    protected static final String JOB_CONFIG = "config";
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledJob.class);
    private boolean enabled = true;

    private String jobName;

    private String triggerName;

    private String jobDescription;

    private JobConfiguration jobConfiguration;

    private DateTime startUpDelay;

    public ScheduledJob() {
        this.jobConfiguration = jobConfiguration.setModified(true);
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        Long start = System.currentTimeMillis();
        try {
            LOGGER.debug(context.getJobDetail().getKey() + " execution starts.");
            setJobConfig(context);
            process(context);
        } catch (Exception ex) {
            LOGGER.error("Error while harvesting data!", ex);
        } finally {
            LOGGER.debug(context.getJobDetail().getKey() + " execution finished in {} ms.",
                    System.currentTimeMillis() - start);
        }
    }

    private void setJobConfig(JobExecutionContext context) {
        if (this.jobConfiguration == null) {
            this.jobConfiguration = (JobConfiguration) context.getJobDetail().getJobDataMap().get(JOB_CONFIG);
        }
    }

    protected abstract void process(JobExecutionContext context) throws JobExecutionException;

    // XXX job details create a job instance! snake biting tail
    public abstract JobDetail createJobDetails();

    protected JobDataMap getJobDataMap() {
        JobDataMap dataMap = new JobDataMap();
        dataMap.put(JOB_CONFIG, jobConfiguration);
        return dataMap;
    }

    public String getJobConfigurationName() {
        return getJobConfiguration().getName();
    }

    public void init(JobConfiguration initConfig) {
        setJobConfiguration(initConfig);
        setJobName(initConfig.getName());
    }

    public void setJobConfiguration(JobConfiguration jobConfiguration) {
        this.jobConfiguration = jobConfiguration;
    }

    public JobConfiguration getJobConfiguration() {
        return jobConfiguration;
    }

    public String getJobName() {
        return jobName == null || jobName.isEmpty() ? getClass().getSimpleName() : jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getTriggerName() {
        return triggerName == null || triggerName.isEmpty() ? "trigger_" + getJobName() : triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getCronExpression() {
        return jobConfiguration.getCronExpression();
    }

    public void setCronExpression(String cronExpresssion) {
        validate(cronExpresssion);
        if (checkCronExpression(cronExpresssion)) {
            jobConfiguration.setCronExpression(cronExpresssion);
            setModified(true);
        }
    }

    public boolean isTriggerAtStartup() {
        return jobConfiguration.isTriggerAtStartup() || isStartUpDelay();
    }

    public boolean isStartUpDelay() {
        return getStartUpDelay() != null && getStartUpDelay().isAfter(DateTime.now());
    }

    public Trigger createTrigger(JobKey jobKey) {
        TriggerBuilder<Trigger> tb = TriggerBuilder.newTrigger().forJob(jobKey).withIdentity(getTriggerName());
        if (getCronExpression() != null) {
            tb.withSchedule(CronScheduleBuilder.cronSchedule(getCronExpression()));
        }

        if (isTriggerAtStartup()) {
            tb.startAt(isStartUpDelay() ? getStartUpDelay().toDate()
                    : DateBuilder.futureDate(5, DateBuilder.IntervalUnit.SECOND));
        }
        return tb.build();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isModified() {
        return getJobConfiguration().isModified();
    }

    public void setModified(boolean modified) {
        getJobConfiguration().setModified(modified);
    }

    @SuppressFBWarnings("EI_EXPOSE_REP")
    public DateTime getStartUpDelay() {
        return startUpDelay;
    }

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public void setStartUpDelay(DateTime startUpDelay) {
        this.startUpDelay = startUpDelay;
    }

    private boolean checkCronExpression(String cronExpression) {
        if (getCronExpression() == null || getCronExpression() != null && !getCronExpression().isEmpty()
                && !getCronExpression().equals(cronExpression)) {
            setCronExpression(cronExpression);
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(ScheduledJob o) {
        return o == null ? -1 : getJobName().compareTo(o.getJobName());
    }
}
