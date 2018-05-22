package com.humming.asc.sales.model;

public class DBTask {

    private int Id;

    private String taskId;
    private String customerRowId;
    private String accountName;
    private String customerCode;
    private String taskName;
    private String startDate;
    private String endDate;
    private String type;
    private String status;
    private String description;
    private String remark;
    private String createTime;
    private String saleName;
    private String creator;

    public DBTask(String taskId, String customerRowId, String taskName, String accountName, String customerCode, String endDate, String startDate, String type, String status, String remark, String description, String saleName, String createTime,String creator) {
        this.taskId = taskId;
        this.customerRowId = customerRowId;
        this.taskName = taskName;
        this.accountName = accountName;
        this.customerCode = customerCode;
        this.endDate = endDate;
        this.startDate = startDate;
        this.type = type;
        this.status = status;
        this.remark = remark;
        this.description = description;
        this.saleName = saleName;
        this.createTime = createTime;
        this.creator = creator;
    }

    public DBTask() {
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getCustomerRowId() {
        return customerRowId;
    }

    public void setCustomerRowId(String customerRowId) {
        this.customerRowId = customerRowId;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSaleName() {
        return saleName;
    }

    public void setSaleName(String saleName) {
        this.saleName = saleName;
    }
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    @Override
    public String toString() {
        return "DBTask{" +
                "taskId='" + taskId + '\'' +
                ", customerRowId='" + customerRowId + '\'' +
                ", accountName='" + accountName + '\'' +
                ", customerCode='" + customerCode + '\'' +
                ", taskName='" + taskName + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", description='" + description + '\'' +
                ", remark='" + remark + '\'' +
                ", createTime='" + createTime + '\'' +
                ", saleName='" + saleName + '\'' +
                '}';
    }
}
