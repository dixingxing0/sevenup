/**
 * JobLog.java 4:37:02 PM Mar 26, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package entity;

import memo.db.Dao;
import memo.db.annotation.Table;




/**
 * 
 * 
 * @author dixingxing	
 * @date Mar 26, 2012
 */
@Table(name ="Job_Log")
public class JobLog extends Dao<JobLog>{
	private Long id;
	private Long jobId;
	/**执行时间*/
	private String time;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getJobId() {
		return jobId;
	}
	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	
}
