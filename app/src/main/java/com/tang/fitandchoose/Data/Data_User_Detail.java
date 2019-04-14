package com.tang.fitandchoose.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tang.fitandchoose.item.Item_user_detail;

import java.util.Date;

import java.util.ArrayList;
import java.util.List;

// 資料功能類別
public class Data_User_Detail {
    // 表格名稱
    public static final String TABLE_NAME = "data_user_detail";

    // 編號表格欄位名稱，固定不變
    public static final String KEY_ID = "_id";

    // 其它表格欄位名稱
    public static final String USER_COLUMN = "_user";
    public static final String DATE_COLUMN = "_date";
    public static final String CATEGORY_COLUMN = "_category";
    public static final String ITEM_COLUMN = "_item";
    public static final String KCAL_COLUMN = "_kcal";
    public static final String NUMBER_COLUMN = "_number";

    // 使用上面宣告的變數建立表格的SQL指令
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    USER_COLUMN + " TEXT NOT NULL, " +
                    DATE_COLUMN + " INTEGER NOT NULL, " +
                    CATEGORY_COLUMN + " TEXT NOT NULL, " +
                    ITEM_COLUMN + " TEXT NOT NULL, " +
                    KCAL_COLUMN + " INTEGER NOT NULL, " +
                    NUMBER_COLUMN + " INTEGER NOT NULL" + ");";

    // 資料庫物件
    private SQLiteDatabase db;

    // 建構子，一般的應用都不需要修改
    public Data_User_Detail(Context context){
        db = MyDBHelper.getDatabase(context);
    }

    // 關閉資料庫，一般的應用都不需要修改
    public void close() {
        db.close();
    }

    // 新增參數指定的物件
    public Item_user_detail insert(Item_user_detail item) {
        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(USER_COLUMN, item.getUser());
        cv.put(DATE_COLUMN, item.getDate());
        cv.put(CATEGORY_COLUMN, item.getCategory());
        cv.put(ITEM_COLUMN, item.getItem());
        cv.put(KCAL_COLUMN, item.getKcal());
        cv.put(NUMBER_COLUMN, item.getNumber());

        // 新增一筆資料並取得編號
        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        long id = db.insert(TABLE_NAME, null, cv);

        // 設定編號
        item.setId(id);
        // 回傳結果
        return item;
    }

    // 修改參數指定的物件
    public boolean update(Item_user_detail item) {
        // 建立準備修改資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的修改資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(USER_COLUMN, item.getUser());
        cv.put(DATE_COLUMN, item.getDate());
        cv.put(CATEGORY_COLUMN, item.getCategory());
        cv.put(ITEM_COLUMN, item.getItem());
        cv.put(KCAL_COLUMN, item.getKcal());
        cv.put(NUMBER_COLUMN, item.getNumber());

        // 設定修改資料的條件為編號
        // 格式為「欄位名稱＝資料」
        String where = KEY_ID + "=" + item.getId();

        // 執行修改資料並回傳修改的資料數量是否成功
        return db.update(TABLE_NAME, cv, where, null) > 0;
    }

    // 刪除參數指定編號的資料
    public boolean delete(long id){
        // 設定條件為編號，格式為「欄位名稱=資料」
        String where = KEY_ID + "=" + id;
        // 刪除指定編號資料並回傳刪除是否成功
        return db.delete(TABLE_NAME, where , null) > 0;
    }

    // 取得資料數量
    public int getCount() {
        int result = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);

        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }
        return result;
    }

    // 讀取所有記事資料
    public List<Item_user_detail> getAll() {
        List<Item_user_detail> result = new ArrayList<Item_user_detail>();
        //游標指向該資料表
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null, null);
        //將所有資料轉成Item並添加進List
        while (cursor.moveToNext()) {
            result.add(getRecord(cursor));
        }
        //關閉游標
        cursor.close();
        return result;
    }



    public int count_all_date(String user) {
        int result = 0;
        String[] colum ={ "COUNT(DISTINCT " + DATE_COLUMN + ")"};
        String where = USER_COLUMN + "=?" ;
        String[] temp = {user};
        //Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME +" "+ where, temp);
        Cursor cursor = db.query(TABLE_NAME, colum, where, temp, null, null, null, null);

        if(cursor == null)
            return result;

        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }
        return result;
    }

    public List<Item_user_detail> search_all_date(String user) {
        List<Item_user_detail> result = new ArrayList<Item_user_detail>();
        //游標指向該資料表
        String[] colum ={ "DISTINCT " + DATE_COLUMN};
        String where = USER_COLUMN + "=?" ;
        String[] temp = {user};
        Cursor cursor = db.query(TABLE_NAME, colum, where, temp, null, null, null, null);
        //將所有資料轉成Item並添加進List
        //if(cursor == null)
        //return result;
        while (cursor.moveToNext()) {
            result.add(getRecord_data(cursor));
        }
        //關閉游標
        cursor.close();
        return result;
    }

    public Item_user_detail getRecord_data(Cursor cursor) {
        // 準備回傳結果用的物件
        Item_user_detail result = new Item_user_detail();
        result.setId(0);
        result.setUser("0");
        result.setDate(cursor.getString(0));
        result.setCategory("0");
        result.setItem("0");
        result.setKcal(0);
        result.setNumber(0);
        return result;
    }

    // 取得指定編號的資料物件
    public Item_user_detail get(long id) {
        // 準備回傳結果用的物件
        Item_user_detail item = null;
        // 使用編號為查詢條件
        String where = KEY_ID + "=" + id;
        // 執行查詢
        Cursor result = db.query(TABLE_NAME, null, where, null, null, null, null, null);

        // 如果有查詢結果
        if (result.moveToFirst()) {
            // 讀取包裝一筆資料的物件
            item = getRecord(result);
        }

        // 關閉Cursor物件
        result.close();
        // 回傳結果
        return item;
    }

    // 把游標Cursor取得的資料轉換成目前的資料包裝為物件
    public Item_user_detail getRecord(Cursor cursor) {
        // 準備回傳結果用的物件
        Item_user_detail result = new Item_user_detail();
        result.setId(cursor.getLong(0));
        result.setUser(cursor.getString(1));
        result.setDate(cursor.getString(2));
        result.setCategory(cursor.getString(3));
        result.setItem(cursor.getString(4));
        result.setKcal(cursor.getLong(5));
        result.setNumber(cursor.getLong(6));
        return result;
    }

    public void delete(String date,String item,String number) {
        // 設定條件為編號，格式為「欄位名稱=資料」
        String where = DATE_COLUMN +  " = ? AND " + ITEM_COLUMN +  " = ? AND " +NUMBER_COLUMN + " = ?";
        String[] temp = {date,item,number};
        //db.delete(TABLE_NAME, where , null);
        db.delete(TABLE_NAME, where , temp);
        // 刪除指定編號資料並回傳刪除是否成功
    }

    public int search_getCount(String date,String user,String catagory) {
        int result = 0;
        String where =   DATE_COLUMN + " =? AND "+USER_COLUMN + " =? AND "+CATEGORY_COLUMN + " =? " ;
        String[] temp = {date,user,catagory};
        String[] ttt = {"COUNT(*)"};
        //Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME +" "+ where, temp);
        Cursor cursor = db.query(TABLE_NAME, ttt, where, temp, null, null, null, null);

        if(cursor == null)
            return result;

        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }
        return result;
    }

    public List<Item_user_detail> search_getAll(String date,String user,String catagory) {
        List<Item_user_detail> result = new ArrayList<Item_user_detail>();
        //游標指向該資料表
        String where =   DATE_COLUMN + "=? AND "+USER_COLUMN + " =? AND "+CATEGORY_COLUMN + " =? " ;
        String[] temp = {date,user,catagory};
        Cursor cursor = db.query(TABLE_NAME, null, where, temp, null, null, null, null);
        //將所有資料轉成Item並添加進List
        if(cursor == null)
            return result;
        while (cursor.moveToNext()) {
            result.add(getRecord(cursor));
        }
        //關閉游標
        cursor.close();
        return result;
    }
    // 建立範例資料
    public void sample() {
    }
}
