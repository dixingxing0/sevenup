/**
 * Job.java 4:27:42 PM Mar 26, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import memo.db.Dao;

/**
 * 
 * 
 * @author dixingxing	
 * @date Mar 26, 2012
 */
public class Job extends Dao<Job>{
	private Long id;
	private String name;
	private String groups;
	/**quartz任务的正则表达式*/
	private String cron;

    private String content;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGroups() {
		return groups;
	}
	public void setGroups(String groups) {
		this.groups = groups;
	}
	public String getCron() {
		return cron;
	}
	public void setCron(String cron) {
		this.cron = cron;
	}

    public String getContent() {
		return cron;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public static void main(String[] args) {
		Job job = new Job();
		job.name = "job_name3";
		job.groups = "job_groups3";
		job.cron = "0/5 * * * * ?";
        job.content = "这是个测试提醒";
		job.insert();
		
		JobLog log = new JobLog();
		log.setJobId(2L);
		log.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//		log.insert();
	}
	
}
