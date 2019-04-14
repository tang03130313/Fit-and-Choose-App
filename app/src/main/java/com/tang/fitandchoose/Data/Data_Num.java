package com.tang.fitandchoose.Data;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tang.fitandchoose.item.Item_num;


// 資料功能類別
public class Data_Num {
    // 表格名稱
    public static final String TABLE_NAME = "data_num";

    // 編號表格欄位名稱，固定不變
    public static final String KEY_ID = "_id";

    // 其它表格欄位名稱
    public static final String NUMBER_COLUMN = "_number";

    // 使用上面宣告的變數建立表格的SQL指令
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NUMBER_COLUMN + " INTEGER NOT NULL);";

    // 資料庫物件
    private SQLiteDatabase db;

    // 建構子，一般的應用都不需要修改
    public Data_Num(Context context){
        db = MyDBHelper.getDatabase(context);
    }

    // 關閉資料庫，一般的應用都不需要修改
    public void close() {
        db.close();
    }

    // 新增參數指定的物件
    public Item_num insert(Item_num item) {
        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
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
    public boolean update(Item_num item) {
        // 建立準備修改資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的修改資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
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

    // 讀取所有記事資料
    public List<Item_num> getAll() {
        List<Item_num> result = new ArrayList<Item_num>();
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

    // 取得指定編號的資料物件
    public Item_num get(long id) {
        // 準備回傳結果用的物件
        Item_num item = null;
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
    public Item_num getRecord(Cursor cursor) {
        // 準備回傳結果用的物件
        Item_num result = new Item_num();
        result.setId(cursor.getLong(0));
        result.setNumber(cursor.getLong(1));
        return result;
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

    // 建立範例資料
    public void sample() {
        Item_num item = new Item_num(0,1);
        Item_num item2 = new Item_num(0,2);
        Item_num item3 = new Item_num(0,3);
        Item_num item4 = new Item_num(0,4);
        Item_num item5 = new Item_num(0,5);
        Item_num item6 = new Item_num(0,6);
        Item_num item7 = new Item_num(0,7);
        Item_num item8 = new Item_num(0,8);
        Item_num item9 = new Item_num(0,9);
        Item_num item10 = new Item_num(0,10);
        insert(item);
        insert(item2);
        insert(item3);
        insert(item4);
        insert(item5);
        insert(item6);
        insert(item7);
        insert(item8);
        insert(item9);
        insert(item10);

        item = new Item_num(0,11);
        item2 = new Item_num(0,12);
        item3 = new Item_num(0,13);
        item4 = new Item_num(0,14);
        item5 = new Item_num(0,15);
        item6 = new Item_num(0,16);
        item7 = new Item_num(0,17);
        item8 = new Item_num(0,18);
        item9 = new Item_num(0,19);
        item10 = new Item_num(0,20);
        insert(item);
        insert(item2);
        insert(item3);
        insert(item4);
        insert(item5);
        insert(item6);
        insert(item7);
        insert(item8);
        insert(item9);
        insert(item10);
        item = new Item_num(0,21);
        item2 = new Item_num(0,22);
        item3 = new Item_num(0,23);
        item4 = new Item_num(0,24);
        item5 = new Item_num(0,25);
        item6 = new Item_num(0,26);
        item7 = new Item_num(0,27);
        item8 = new Item_num(0,28);
        item9 = new Item_num(0,29);
        item10 = new Item_num(0,30);
        insert(item);
        insert(item2);
        insert(item3);
        insert(item4);
        insert(item5);
        insert(item6);
        insert(item7);
        insert(item8);
        insert(item9);
        insert(item10);
    }
}
