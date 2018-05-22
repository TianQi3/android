package com.humming.asc.sales.datebase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatebaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "asc.db";//数据库名
    public static final String TABLE_NAME_TASK = "TaskListTable";//表名
    public static final String TABLE_NAME_DAILYCALL = "DailyCallListTable";//表名
    public static final String TABLE_NAME_LEADS = "LeadsListTable";//表名
    private static final int version = 2;
    private Context context;

    public DatebaseHelper(Context context) {
        super(context, DB_NAME, null, version);
        this.context = context;
    }

    //在第一次调用DatabaseHelper类时会检查数据库是否存在
    //如果不存在调用onCreate方法
    //所以，我们要在该方法中实现数据库的创建
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_TASK + "(id INTEGER PRIMARY KEY AUTOINCREMENT,taskId TEXT,customerRowId TEXT,accountName TEXT,customerCode TEXT, taskName TEXT,startDate TEXT,endDate TEXT,type TEXT,status TEXT,description TEXT,remark TEXT,createTime TEXT,saleName TEXT)";
        db.execSQL(sql);
        String sql2 = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_DAILYCALL + "(id INTEGER PRIMARY KEY AUTOINCREMENT,remark TEXT, rowId TEXT,taskId TEXT,accountName TEXT,customerRowId TEXT,assocType TEXT,meetingContent TEXT,result TEXT,note TEXT,taskName TEXT,startTime TEXT,endTime TEXT,type TEXT,status TEXT,location TEXT,subject TEXT,lastUpd TEXT,pics TEXT,createTime TEXT,saleName TEXT)";
        db.execSQL(sql2);
        String sql3 = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_LEADS + "(id INTEGER PRIMARY KEY AUTOINCREMENT,remark TEXT,rowId TEXT,targetFlg TEXT,nameEn TEXT,nameCn TEXT,saleName TEXT,saleEid TEST,classes TEXT,status TEXT,region TEXT,province TEXT,provinceId TEXT,city TEXT,channel TEXT,channelId TEXT,address TEXT,subChannel TEXT,chain TEXT,chainId TEXT,subChain TEXT,conFstName TEXT,conLastName TEXT,jobTitle TEXT,workPhNum TEXT,cellPhNum TEXT,email TEXT,acntId TEXT,accountName TEXT,stage TEXT,actDate TEXT,actSales TEXT,estDate TEXT,estSales TEXT,demand TEXT,devPlan TEXT,tag1 TEXT,tag2 TEXT,tag3 TEXT,tag4 TEXT,tag5 TEXT,createTime TEXT)";
        db.execSQL(sql3);
    }

    //如果数据库版本发生变化，（通过检查数据库版本标记）如果版本发生变化
    //那么会调用onupgrade方法
    //所以在该方法中实现数据库的更新操作
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TASK);
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_TASK + "(id INTEGER PRIMARY KEY AUTOINCREMENT,taskId TEXT,customerRowId TEXT,accountName TEXT,customerCode TEXT, taskName TEXT,startDate TEXT,endDate TEXT,type TEXT,status TEXT,description TEXT,remark TEXT,createTime TEXT,saleName TEXT,creator TEXT)";
            db.execSQL(sql);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_DAILYCALL);
            String sql2 = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_DAILYCALL + "(id INTEGER PRIMARY KEY AUTOINCREMENT,remark TEXT, rowId TEXT,taskId TEXT,accountName TEXT,customerRowId TEXT,assocType TEXT,meetingContent TEXT,result TEXT,note TEXT,taskName TEXT,startTime TEXT,endTime TEXT,type TEXT,status TEXT,location TEXT,subject TEXT,lastUpd TEXT,pics TEXT,createTime TEXT,saleName TEXT,creator TEXT)";
            db.execSQL(sql2);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_LEADS);
            String sql3 = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_LEADS + "(id INTEGER PRIMARY KEY AUTOINCREMENT,remark TEXT,rowId TEXT,targetFlg TEXT,nameEn TEXT,nameCn TEXT,saleName TEXT,saleEid TEST,classes TEXT,status TEXT,region TEXT,province TEXT,provinceId TEXT,city TEXT,channel TEXT,channelId TEXT,address TEXT,subChannel TEXT,chain TEXT,chainId TEXT,subChain TEXT,conFstName TEXT,conLastName TEXT,jobTitle TEXT,workPhNum TEXT,cellPhNum TEXT,email TEXT,acntId TEXT,accountName TEXT,stage TEXT,actDate TEXT,actSales TEXT,estDate TEXT,estSales TEXT,demand TEXT,devPlan TEXT,tag1 TEXT,tag2 TEXT,tag3 TEXT,tag4 TEXT,tag5 TEXT,createTime TEXT,creator TEXT)";
            db.execSQL(sql3);
        }
    }

}
