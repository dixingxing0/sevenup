/**
 * HelloJob.java 11:31:21 AM Feb 14, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package schedule;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;

public class SimpleJob implements Job {
	private final static Logger logger = Logger.getLogger(SimpleJob.class);

	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		JobDetail job = context.getJobDetail();
		Trigger trigger = context.getTrigger();

		String s = new SimpleDateFormat("HH:mm:ss").format(new Date());

		logger.info(String.format("%s - %s , %s", s, job.getKey(), trigger
				.getKey()));
	}
}
