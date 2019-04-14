package com.tang.fitandchoose.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {

    // 資料庫名稱
    public static final String DATABASE_NAME = "mydatabase_test6.db";
    // 資料庫版本，資料結構改變的時候要更改這個數字，通常是加一
    public static int VERSION = 9;
    // 資料庫物件，固定的欄位變數
    private static SQLiteDatabase database;

    //context為當前activity的上下文,name為要操作的資料庫名稱,factory用來做深入查詢用,version為版本
    public MyDBHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
    }
    //只有在資料庫不存在時,才會呼叫onCreate()建立資料庫
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        // 建立要存放資料的資料表格
        // 1. SQL語法不分大小寫
        // 2. 這裡大寫代表的是SQL標準語法, 小寫字是資料表/欄位的命名

        // 建立應用程式需要的表格
        db.execSQL(Data_Num.CREATE_TABLE);
        db.execSQL(Data_Meals.CREATE_TABLE);
        db.execSQL(Data_Item.CREATE_TABLE);
        db.execSQL(Data_Sport.CREATE_TABLE);
        db.execSQL(Data_User_Detail.CREATE_TABLE);
        //db.execSQL(Test_Data.CREATE_TABLE);

    }//
    //使用建構子時如果版本增加,便會呼叫onUpgrade()刪除舊的資料表與其內容,再重新呼叫onCreate()建立新的資料表
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        // 刪除原有的表格
        //db.execSQL("DROP TABLE IF EXISTS " + Test_Data.TABLE_NAME);
        // 呼叫onCreate建立新版的表格
        //onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS " + Data_Num.TABLE_NAME);
        // 呼叫onCreate建立新版的表格
        db.execSQL("DROP TABLE IF EXISTS " + Data_Meals.TABLE_NAME);
        // 呼叫onCreate建立新版的表格
        db.execSQL("DROP TABLE IF EXISTS " + Data_Item.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Data_Sport.TABLE_NAME);
        // 呼叫onCreate建立新版的表格
        db.execSQL("DROP TABLE IF EXISTS " + Data_User_Detail.TABLE_NAME);
        onCreate(db);
    }

    // 需要資料庫的元件呼叫這個方法，這個方法在一般的應用都不需要修改
    public static SQLiteDatabase getDatabase(Context context) {
        if (database == null || !database.isOpen()) {
            database = new MyDBHelper(context, DATABASE_NAME, null, VERSION).getWritableDatabase();
        }
        return database;
    }
}