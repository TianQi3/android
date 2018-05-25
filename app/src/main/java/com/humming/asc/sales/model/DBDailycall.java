package com.humming.asc.sales.model;

public class DBDailycall {
	private String remark;
	private String rowId;
	private String taskId;
	private String accountName;
	private String customerRowId;
	private String assocType;
	private String meetingContent;
	private String result;
	private String note;
	private String taskName;
	private String startTime;
	private String endTime;
	private String type;
	private String status;
	private String location;
	private String subject;
	private String lastUpd;
	private String pics;
	private String createTime;
	private String saleName;
	private int Id;
	private String creator;
	private String acRowId;
	private String acName;

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRowId() {
		return rowId;
	}

	public void setRowId(String rowId) {
		this.rowId = rowId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAssocType() {
		return assocType;
	}

	public void setAssocType(String assocType) {
		this.assocType = assocType;
	}

	public String getCustomerRowId() {
		return customerRowId;
	}

	public void setCustomerRowId(String customerRowId) {
		this.customerRowId = customerRowId;
	}

	public String getMeetingContent() {
		return meetingContent;
	}

	public void setMeetingContent(String meetingContent) {
		this.meetingContent = meetingContent;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSaleName() {
		return saleName;
	}

	public void setSaleName(String saleName) {
		this.saleName = saleName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getLastUpd() {
		return lastUpd;
	}

	public void setLastUpd(String lastUpd) {
		this.lastUpd = lastUpd;
	}

	public String getPics() {
		return pics;
	}

	public void setPics(String pics) {
		this.pics = pics;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public DBDailycall() {

	}

	public String getAcRowId() {
		return acRowId;
	}

	public void setAcRowId(String acRowId) {
		this.acRowId = acRowId;
	}

	public String getAcName() {
		return acName;
	}

	public void setAcName(String acName) {
		this.acName = acName;
	}

	public DBDailycall(String remark, String rowId, String taskId, String accountName, String customerRowId, String assocType, String meetingContent, String result, String note, String taskName, String startTime, String endTime, String type, String status, String location, String subject, String lastUpd, String pics, String createTime, String saleName,String creator,String acRowId, String acName) {
		this.remark = remark;
		this.rowId = rowId;
		this.taskId = taskId;
		this.accountName = accountName;
		this.customerRowId = customerRowId;
		this.assocType = assocType;
		this.meetingContent = meetingContent;
		this.result = result;
		this.note = note;
		this.taskName = taskName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.type = type;
		this.status = status;
		this.location = location;
		this.subject = subject;
		this.lastUpd = lastUpd;
		this.pics = pics;
		this.createTime = createTime;
		this.saleName = saleName;
		this.creator = creator;
		this.acRowId = acRowId;
		this.acName = acName;
	}
}
