package com.tang.fitandchoose.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tang.fitandchoose.item.Item_item;
import com.tang.fitandchoose.item.Item_sport;

import java.util.ArrayList;
import java.util.List;

// 資料功能類別
public class Data_Sport {
    // 表格名稱
    public static final String TABLE_NAME = "data_sport";

    // 編號表格欄位名稱，固定不變
    public static final String KEY_ID = "_id";

    // 其它表格欄位名稱
    public static final String NAME_COLUMN = "_name";
    public static final String KCAL_COLUMN = "_kcal";

    // 使用上面宣告的變數建立表格的SQL指令
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NAME_COLUMN + " TEXT NOT NULL, " +
                    KCAL_COLUMN + " INTEGER NOT NULL" +");";

    // 資料庫物件
    private SQLiteDatabase db;

    // 建構子，一般的應用都不需要修改
    public Data_Sport(Context context){
        db = MyDBHelper.getDatabase(context);
    }

    // 關閉資料庫，一般的應用都不需要修改
    public void close() {
        db.close();
    }

    // 新增參數指定的物件
    public Item_sport insert(Item_sport item) {
        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(NAME_COLUMN, item.getName());
        cv.put(KCAL_COLUMN, item.getKcal());

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
    public boolean update(Item_sport item) {
        // 建立準備修改資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的修改資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(NAME_COLUMN, item.getName());
        cv.put(KCAL_COLUMN, item.getKcal());

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
    public List<Item_sport> getAll() {
        List<Item_sport> result = new ArrayList<Item_sport>();
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
    public Item_sport get(long id) {
        // 準備回傳結果用的物件
        Item_sport item = null;
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
    public Item_sport getRecord(Cursor cursor) {
        // 準備回傳結果用的物件
        Item_sport result = new Item_sport();
        result.setId(cursor.getLong(0));
        result.setName(cursor.getString(1));
        result.setKcal(cursor.getLong(2));
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

    public int search_getCount(String item) {
        int result = 0;
        String where =   NAME_COLUMN + " LIKE ? " ;
        String like = "%" + item + "%";
        String[] temp = {like};
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

    public List<Item_sport> search_getAll(String item) {
        List<Item_sport> result = new ArrayList<Item_sport>();
        //游標指向該資料表
        String where =   NAME_COLUMN +" LIKE ? " ;
        String[] temp = {"%" + item + "%"};
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
        Item_sport item = new Item_sport(0,"冰上曲棍球-1小時",504);
        Item_sport item2 = new Item_sport(0,"曲棍球-1小時",504);
        Item_sport item3 = new Item_sport(0,"乒乓球-1小時",252);
        Item_sport item4 = new Item_sport(0,"羽毛球-1小時",504);
        Item_sport item5 = new Item_sport(0,"足球-1小時",504);
        Item_sport item6 = new Item_sport(0,"排球-1小時",252);
        Item_sport item7 = new Item_sport(0,"網球-1小時",504);
        Item_sport item8 = new Item_sport(0,"籃球-1小時",378);
        Item_sport item9 = new Item_sport(0,"跑步-30分鐘 (10公里/小時)",315);
        Item_sport item10 = new Item_sport(0,"跑步-30分鐘 (慢跑-8公里/小時)",252);
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

        item = new Item_sport(0,"跳繩-30分鐘",267);
        item2 = new Item_sport(0,"腳踏車-1小時",222);
        item3 = new Item_sport(0,"逛街購物-1小時",216);
        item4 = new Item_sport(0,"爬樓梯-30分鐘",174);
        item5 = new Item_sport(0,"蛙式游泳-30分鐘",354);
        item6 = new Item_sport(0,"自由式游泳-30分鐘",525);
        item7 = new Item_sport(0,"有氧舞蹈-1小時",300);
        item8 = new Item_sport(0,"呼拉圈-1小時",138);
        item9 = new Item_sport(0,"睡眠-16小時",1008);
        item10 = new Item_sport(0,"睡眠-8小時",454);
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
