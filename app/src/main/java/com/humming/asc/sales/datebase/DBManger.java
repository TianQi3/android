package com.humming.asc.sales.datebase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.humming.asc.sales.model.DBDailycall;
import com.humming.asc.sales.model.DBLeads;
import com.humming.asc.sales.model.DBTask;

import java.util.ArrayList;
import java.util.List;


public class DBManger {
    private Context context;
    private DatebaseHelper helper;

    public DBManger(Context context) {
        this.helper = new DatebaseHelper(context);
        this.context = context;
    }

    public void InsertTaskListTable(DBTask dbTask) {
        SQLiteDatabase db = helper.getWritableDatabase();
        //方式二
        String sql = "insert into " + DatebaseHelper.TABLE_NAME_TASK + "(taskId,customerRowId,accountName,customerCode,taskName,startDate,endDate,type,status,description,remark,createTime,saleName,creator)values('" + dbTask.getTaskId() + "','" + dbTask.getCustomerRowId() + "','" + dbTask.getAccountName() + "','" + dbTask.getCustomerCode() + "','" + dbTask.getTaskName() + "','" + dbTask.getStartDate() + "','" + dbTask.getEndDate() + "','" + dbTask.getType() +
                "','" + dbTask.getStatus() + "','" + dbTask.getDescription() + "','" + dbTask.getRemark() + "','" + dbTask.getCreateTime() + "','" + dbTask.getSaleName() + "','" + dbTask.getCreator() + "')";
        db.execSQL(sql);
        db.close();
    }

    public void InsertDailyCallListTable(DBDailycall dbDailycall) {
        SQLiteDatabase db = helper.getWritableDatabase();
        //方式二
        String sql = "insert into " + DatebaseHelper.TABLE_NAME_DAILYCALL + "(remark,rowId,taskId,accountName,customerRowId,assocType,meetingContent,result,note,taskName,startTime,endTime,type,status,location,subject,lastUpd,pics,createTime,saleName,creator,acRowId,acName)values('"
                + dbDailycall.getRemark() + "','" + dbDailycall.getRowId() + "','" + dbDailycall.getTaskId() + "','" + dbDailycall.getAccountName() + "','" + dbDailycall.getCustomerRowId() + "','" + dbDailycall.getAssocType() + "','" + dbDailycall.getMeetingContent() + "','" + dbDailycall.getResult() +
                "','" + dbDailycall.getNote() + "','" + dbDailycall.getTaskName() + "','" + dbDailycall.getStartTime() + "','" + dbDailycall.getEndTime() + "','" + dbDailycall.getType() + "','" + dbDailycall.getStatus() + "','" + dbDailycall.getLocation() + "','" + dbDailycall.getSubject() + "','" + dbDailycall.getLastUpd()
                + "','" + dbDailycall.getPics() + "','" + dbDailycall.getCreateTime() + "','" + dbDailycall.getSaleName() + "','" + dbDailycall.getCreator() + "','" + dbDailycall.getAcRowId() + "','" + dbDailycall.getAcName() + "')";
        db.execSQL(sql);
        db.close();
    }

    public void InsertLeadsListTable(DBLeads dbLeads) {
        SQLiteDatabase db = helper.getWritableDatabase();
        //方式二
        String sql = "insert into " + DatebaseHelper.TABLE_NAME_LEADS + "(remark,rowId,targetFlg,nameEn,nameCn,saleEid,saleName,classes,status,region,province,provinceId,city,channel,channelId,address,subChannel,chain,chainId,subChain,conFstName," +
                "conLastName,jobTitle,workPhNum,cellPhNum,email,acntId,accountName,stage,actDate,actSales,estDate,estSales,demand,devPlan,tag1,tag2,tag3,tag4,tag5,createTime,creator)values('"
                + dbLeads.getRemark() + "','" + dbLeads.getRowId() + "','" + dbLeads.getTargetFlg() + "','" + dbLeads.getNameEn() + "','" + dbLeads.getNameCn() + "','" + dbLeads.getSaleEid() + "','" + dbLeads.getSaleName() + "','" + dbLeads.getClasses() + "','" +
                dbLeads.getStatus() + "','" + dbLeads.getRegion() + "','" + dbLeads.getProvince() + "','" + dbLeads.getProvinceId() + "','" + dbLeads.getCity() + "','" + dbLeads.getChannel() + "','" + dbLeads.getChannelId() + "','" + dbLeads.getAddress() + "','" + dbLeads.getSubChannel() + "','" + dbLeads.getChain()
                + "','" + dbLeads.getChainId() + "','" + dbLeads.getSubChain() + "','" + dbLeads.getConFstName() + "','" + dbLeads.getConLastName() + "','" + dbLeads.getJobTitle() + "','" + dbLeads.getCellPhNum() + "','" + dbLeads.getWorkPhNum() + "','" + dbLeads.getEmail() + "','" + dbLeads.getAcntId() + "','" + dbLeads.getAccountName() + "','" + dbLeads.getStage() + "','" + dbLeads.getActDate()
                + "','" + dbLeads.getActSales() + "','" + dbLeads.getEstDate() + "','" + dbLeads.getEstSales() + "','" + dbLeads.getDemand() + "','" + dbLeads.getDevPlan() + "','" + dbLeads.getTag1() + "','" + dbLeads.getTag2() + "','" + dbLeads.getTag3() + "','" + dbLeads.getTag4() + "','" + dbLeads.getTag5() + "','" + dbLeads.getCreateTime() + "','" + dbLeads.getCreator() + "')";
        db.execSQL(sql);
        db.close();
    }

    //delete task
    public void DeleteTaskById(int id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        //方式二
        String sql = "DELETE FROM TaskListTable WHERE id=" + id;
        db.execSQL(sql);
        db.close();
    }

    //delete dailycal
    public void DeleteDailyCallById(int id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        //方式二
        String sql = "DELETE FROM DailyCallListTable WHERE id=" + id;
        db.execSQL(sql);
        db.close();
    }

    //delete Leads
    public void DeleteLeads(int id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        //方式二
        String sql = "DELETE FROM LeadsListTable WHERE id=" + id;
        db.execSQL(sql);
        db.close();
    }

    //update task
    public void UpdateTaskById(DBTask dbTask, int id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        //方式二
        String sql = "update " + DatebaseHelper.TABLE_NAME_TASK + " set taskId='" + dbTask.getTaskId() + "',customerRowId='" + dbTask.getCustomerRowId()
                + "',customerCode='" + dbTask.getCustomerCode() + "',taskName='" + dbTask.getTaskName() + "',startDate='" + dbTask.getStartDate()
                + "',endDate='" + dbTask.getEndDate() + "',type='" + dbTask.getType() + "',status='" + dbTask.getStatus()
                + "',description='" + dbTask.getDescription() + "',remark='" + dbTask.getRemark() + "',createTime='" + dbTask.getCreateTime()
                + "',creator='" + dbTask.getCreator()
                + "' where id=" + id;
        db.execSQL(sql);
        db.close();
    }

    //update dailycall
    public void UpdateDailyCallById(DBDailycall dbDailycall, int id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        //方式二
        String sql = "update " + DatebaseHelper.TABLE_NAME_DAILYCALL + " set rowId='" + dbDailycall.getRowId() + "',taskId='" + dbDailycall.getTaskId()
                + "',accountName='" + dbDailycall.getAccountName() + "',customerRowId='" + dbDailycall.getCustomerRowId() + "',assocType='" + dbDailycall.getAssocType()
                + "',meetingContent='" + dbDailycall.getMeetingContent() + "',result='" + dbDailycall.getResult() + "',note='" + dbDailycall.getNote()
                + "',taskName='" + dbDailycall.getTaskName() + "',startTime='" + dbDailycall.getStartTime() + "',endTime='" + dbDailycall.getEndTime()
                + "',type='" + dbDailycall.getType() + "',status='" + dbDailycall.getStatus() + "',location='" + dbDailycall.getLocation()
                + "',subject='" + dbDailycall.getSubject() + "',lastUpd='" + dbDailycall.getLastUpd() + "',pics='" + dbDailycall.getPics()
                + "',createTime='" + dbDailycall.getCreateTime() + "',creator='" + dbDailycall.getCreator() + "',acRowId='" + dbDailycall.getAcRowId() + "',acName='" + dbDailycall.getAcName() + "' where id=" + id;
        db.execSQL(sql);
        db.close();
    }

    //update leads
    public void UpdateLeadsById(DBLeads dbLeads, int id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        //方式二
        String sql = "update " + DatebaseHelper.TABLE_NAME_LEADS + " set rowId='" + dbLeads.getRowId() + "',targetFlg='" + dbLeads.getTargetFlg()
                + "',nameEn='" + dbLeads.getNameEn() + "',nameCn='" + dbLeads.getNameCn() + "',saleEid='" + dbLeads.getSaleEid()
                + "',saleName='" + dbLeads.getSaleName() + "',classes='" + dbLeads.getClasses() + "',status='" + dbLeads.getStatus()
                + "',region='" + dbLeads.getRegion() + "',province='" + dbLeads.getProvince() + "',provinceId='" + dbLeads.getProvinceId()
                + "',city='" + dbLeads.getCity() + "',channel='" + dbLeads.getChannel() + "',channelId='" + dbLeads.getChannelId()
                + "',address='" + dbLeads.getAddress() + "',subChannel='" + dbLeads.getSubChannel() + "',chain='" + dbLeads.getChain()
                + "',chainId='" + dbLeads.getChainId() + "',subChain='" + dbLeads.getSubChain() + "',conFstName='" + dbLeads.getConFstName()
                + "',conLastName='" + dbLeads.getConLastName() + "',jobTitle='" + dbLeads.getJobTitle() + "',workPhNum='" + dbLeads.getWorkPhNum()
                + "',cellPhNum='" + dbLeads.getCellPhNum() + "',email='" + dbLeads.getEmail() + "',acntId='" + dbLeads.getAcntId()
                + "',accountName='" + dbLeads.getAccountName() + "',stage='" + dbLeads.getStage() + "',actDate='" + dbLeads.getActDate()
                + "',actSales='" + dbLeads.getActSales() + "',estDate='" + dbLeads.getEstDate() + "',estSales='" + dbLeads.getEstSales()
                + "',demand='" + dbLeads.getDemand() + "',devPlan='" + dbLeads.getDevPlan() + "',tag1='" + dbLeads.getTag1()
                + "',tag2='" + dbLeads.getTag2() + "',tag3='" + dbLeads.getTag3() + "',tag4='" + dbLeads.getTag4()
                + "',tag5='" + dbLeads.getTag5() + "',createTime='" + dbLeads.getCreateTime() + "',creator='" + dbLeads.getCreator() + "' where id=" + id;
        db.execSQL(sql);
        db.close();
    }


    //select task
    public List<DBTask> SelectTaskList() {
        List<DBTask> dbTaskList = new ArrayList<DBTask>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from " + DatebaseHelper.TABLE_NAME_TASK + " order by createTime desc", null);
        if (c.moveToFirst()) {//判断是否查询到数据
            for (int i = 0; i < c.getCount(); i++) {
                c.moveToPosition(i);//移动到指定目录
                int id = c.getInt(0);
                String taskId = c.getString(1);
                String customerRowId = c.getString(2);
                String accountName = c.getString(3);
                String customerCode = c.getString(4);
                String taskName = c.getString(5);
                String startDate = c.getString(6);
                String endDate = c.getString(7);
                String type = c.getString(8);
                String status = c.getString(9);
                String description = c.getString(10);
                String remark = c.getString(11);
                String createTime = c.getString(12);
                String saleName = c.getString(13);
                String creator = c.getString(14);
                DBTask dbTask = new DBTask();
                dbTask.setTaskId(taskId);
                dbTask.setId(id);
                dbTask.setCustomerRowId(customerRowId);
                dbTask.setAccountName(accountName);
                dbTask.setCustomerCode(customerCode);
                dbTask.setTaskName(taskName);
                dbTask.setStartDate(startDate);
                dbTask.setEndDate(endDate);
                dbTask.setType(type);
                dbTask.setStatus(status);
                dbTask.setDescription(description);
                dbTask.setRemark(remark);
                dbTask.setCreateTime(createTime);
                dbTask.setSaleName(saleName);
                dbTask.setCreator(creator);
                dbTaskList.add(dbTask);
            }
        }
        db.close();
        return dbTaskList;
    }

    //select dailyCall
    public List<DBDailycall> SelectDailyCallList() {
        List<DBDailycall> dbDailycallList = new ArrayList<DBDailycall>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from " + DatebaseHelper.TABLE_NAME_DAILYCALL, null);
        if (c.moveToFirst()) {//判断是否查询到数据
            for (int i = 0; i < c.getCount(); i++) {
                c.moveToPosition(i);//移动到指定目录
                int id = c.getInt(0);
                String remark = c.getString(1);
                String rowId = c.getString(2);
                String taskId = c.getString(3);
                String accountName = c.getString(4);
                String customerRowId = c.getString(5);
                String assocType = c.getString(6);
                String meetingContent = c.getString(7);
                String result = c.getString(8);
                String note = c.getString(9);
                String taskName = c.getString(10);
                String startTime = c.getString(11);
                String endTime = c.getString(12);
                String type = c.getString(13);
                String status = c.getString(14);
                String location = c.getString(15);
                String subject = c.getString(16);
                String lastUpd = c.getString(17);
                String pics = c.getString(18);
                String createTime = c.getString(19);
                String saleName = c.getString(20);
                String creator = c.getString(21);
                String acRowId = c.getString(22);
                String acName = c.getString(23);
                DBDailycall dbDailycall = new DBDailycall();
                dbDailycall.setRemark(remark);
                dbDailycall.setRowId(rowId);
                dbDailycall.setTaskId(taskId);
                dbDailycall.setAccountName(accountName);
                dbDailycall.setCustomerRowId(customerRowId);
                dbDailycall.setAssocType(assocType);
                dbDailycall.setMeetingContent(meetingContent);
                dbDailycall.setResult(result);
                dbDailycall.setNote(note);
                dbDailycall.setTaskName(taskName);
                dbDailycall.setStartTime(startTime);
                dbDailycall.setEndTime(endTime);
                dbDailycall.setType(type);
                dbDailycall.setStatus(status);
                dbDailycall.setLocation(location);
                dbDailycall.setSubject(subject);
                dbDailycall.setLastUpd(lastUpd);
                dbDailycall.setPics(pics);
                dbDailycall.setCreateTime(createTime);
                dbDailycall.setSaleName(saleName);
                dbDailycall.setId(id);
                dbDailycall.setCreator(creator);
                dbDailycall.setAcRowId(acRowId);
                dbDailycall.setAcName(acName);
                dbDailycallList.add(dbDailycall);
            }
        }
        db.close();
        return dbDailycallList;
    }

    //select leads
    public List<DBLeads> SelectLeadsList() {
        List<DBLeads> dbLeadsList = new ArrayList<DBLeads>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from " + DatebaseHelper.TABLE_NAME_LEADS, null);
        if (c.moveToFirst()) {//判断是否查询到数据
            for (int i = 0; i < c.getCount(); i++) {
                c.moveToPosition(i);//移动到指定目录
                int id = c.getInt(0);
                String remark = c.getString(1);
                String rowId = c.getString(2);
                String targetFlg = c.getString(3);
                String nameEn = c.getString(4);
                String nameCn = c.getString(5);
                String saleEid = c.getString(7);
                String saleName = c.getString(6);
                String classes = c.getString(8);
                String status = c.getString(9);
                String region = c.getString(10);
                String province = c.getString(11);
                String provinceId = c.getString(12);
                String city = c.getString(13);
                String channel = c.getString(14);
                String channelId = c.getString(15);
                String address = c.getString(16);
                String subChannel = c.getString(17);
                String chain = c.getString(18);
                String chainId = c.getString(19);
                String subChain = c.getString(20);
                String conFstName = c.getString(21);
                String conLastName = c.getString(22);
                String jobTitle = c.getString(23);
                String workPhNum = c.getString(24);
                String cellPhNum = c.getString(25);
                String email = c.getString(26);
                String acntId = c.getString(27);
                String accountName = c.getString(28);
                String stage = c.getString(29);
                String actDate = c.getString(30);
                String actSales = c.getString(31);
                String estDate = c.getString(32);
                String estSales = c.getString(33);
                String demand = c.getString(34);
                String devPlan = c.getString(35);
                String tag1 = c.getString(36);
                String tag2 = c.getString(37);
                String tag3 = c.getString(38);
                String tag4 = c.getString(39);
                String tag5 = c.getString(40);
                String createTime = c.getString(41);
                String creator = c.getString(42);

                DBLeads dbLeads = new DBLeads();
                dbLeads.setCreator(creator);
                dbLeads.setId(id);
                dbLeads.setRemark(remark);
                dbLeads.setRowId(rowId);
                dbLeads.setTargetFlg(targetFlg);
                dbLeads.setNameEn(nameEn);
                dbLeads.setNameCn(nameCn);
                dbLeads.setSaleEid(saleEid);
                dbLeads.setSaleName(saleName);
                dbLeads.setClasses(classes);
                dbLeads.setStatus(status);
                dbLeads.setRegion(region);
                dbLeads.setProvince(province);
                dbLeads.setProvinceId(provinceId);
                dbLeads.setCity(city);
                dbLeads.setChannel(channel);
                dbLeads.setChannelId(channelId);
                dbLeads.setAddress(address);
                dbLeads.setSubChannel(subChannel);
                dbLeads.setChain(chain);
                dbLeads.setChainId(chainId);
                dbLeads.setSubChain(subChain);
                dbLeads.setConFstName(conFstName);
                dbLeads.setConLastName(conLastName);
                dbLeads.setJobTitle(jobTitle);
                dbLeads.setWorkPhNum(workPhNum);
                dbLeads.setCellPhNum(cellPhNum);
                dbLeads.setEmail(email);
                dbLeads.setAcntId(acntId);
                dbLeads.setAccountName(accountName);
                dbLeads.setStage(stage);
                dbLeads.setActDate(actDate);
                dbLeads.setActSales(actSales);
                dbLeads.setEstDate(estDate);
                dbLeads.setEstSales(estSales);
                dbLeads.setDemand(demand);
                dbLeads.setDevPlan(devPlan);
                dbLeads.setTag1(tag1);
                dbLeads.setTag2(tag2);
                dbLeads.setTag3(tag3);
                dbLeads.setTag4(tag4);
                dbLeads.setTag5(tag5);
                dbLeads.setCreateTime(createTime);
                dbLeadsList.add(dbLeads);
            }
        }
        db.close();
        return dbLeadsList;
    }
}
