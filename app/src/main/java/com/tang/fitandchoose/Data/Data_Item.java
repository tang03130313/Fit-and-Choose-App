package com.tang.fitandchoose.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tang.fitandchoose.item.Item_item;

import java.util.ArrayList;
import java.util.List;

// 資料功能類別
public class Data_Item {
    // 表格名稱
    public static final String TABLE_NAME = "data_item";

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
    public Data_Item(Context context){
        db = MyDBHelper.getDatabase(context);
    }

    // 關閉資料庫，一般的應用都不需要修改
    public void close() {
        db.close();
    }

    // 新增參數指定的物件
    public Item_item insert(Item_item item) {
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
    public boolean update(Item_item item) {
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
    public List<Item_item> getAll() {
        List<Item_item> result = new ArrayList<Item_item>();
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
    public Item_item get(long id) {
        // 準備回傳結果用的物件
        Item_item item = null;
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
    public Item_item getRecord(Cursor cursor) {
        // 準備回傳結果用的物件
        Item_item result = new Item_item();
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

    public List<Item_item> search_getAll(String item) {
        List<Item_item> result = new ArrayList<Item_item>();
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
        Item_item item = new Item_item(0,"炒飯",333);
        Item_item item2 = new Item_item(0,"蛋炒飯",164);
        Item_item item3 = new Item_item(0,"中式炒飯",299);
        Item_item item4 = new Item_item(0,"素食炒飯",251);
        Item_item item5 = new Item_item(0,"漢堡",290);
        Item_item item6 = new Item_item(0,"義大利麵",220);
        Item_item item7 = new Item_item(0,"番茄肉醬義大利麵",310);
        Item_item item8 = new Item_item(0,"雞肉飯",330);
        Item_item item9 = new Item_item(0,"海鮮粥",114);
        Item_item item10 = new Item_item(0,"滷肉飯",187);
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

        item = new Item_item(0,"牛肉麵 (興仁牛肉麵)",542);
        item2 = new Item_item(0,"排骨飯 (興仁牛肉麵)",425);
        item3 = new Item_item(0,"貢丸湯 (興仁牛肉麵)",165);
        item4 = new Item_item(0,"甜鬆餅 (微笑的魚)",400);
        item5 = new Item_item(0,"鹹鬆餅 (微笑的魚)",500);
        item6 = new Item_item(0,"奶油啤酒 (微笑的魚)",162);
        item7 = new Item_item(0,"招牌餐全套 (食之初料理)",1200);
        item8 = new Item_item(0,"招牌餐半套 (食之初料理)",900);
        item9 = new Item_item(0,"家常餐 (食之初料理)",800);
        item10 = new Item_item(0,"豆皮壽司 (緣自壽司屋)",144);
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

        item = new Item_item(0,"玉子燒 (緣自壽司屋)",134);
        item2 = new Item_item(0,"鮪魚沙拉 (緣自壽司屋)",93);
        item3 = new Item_item(0,"鮭魚生魚片 (緣自壽司屋)",94);
        item4 = new Item_item(0,"秋刀魚生魚片 (緣自壽司屋)",98);
        item5 = new Item_item(0,"牛排 (放牛班)",660);
        item6 = new Item_item(0,"雞排 (放牛班)",500);
        item7 = new Item_item(0,"豬排 (放牛班)",600);
        item8 = new Item_item(0,"義大利麵 (Pasta Good Good)",500);
        item9 = new Item_item(0,"燉飯 (Pasta Good Good)",600);
        item10 = new Item_item(0,"焗烤飯/麵 (Pasta Good Good)",800);
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

        item = new Item_item(0,"雞肉咖哩 (元智咖哩匠)",574);
        item2 = new Item_item(0,"豬排咖哩 (元智咖哩匠)",592);
        item3 = new Item_item(0,"醬汁咖哩 (元智咖哩匠)",504);
        item4 = new Item_item(0,"義大利麵 (破舍咖啡)",502);
        item5 = new Item_item(0,"奶茶 (破舍咖啡)",313);
        item6 = new Item_item(0,"水果茶 (破舍咖啡)",142);
        item7 = new Item_item(0,"奶昔 (放牛班)",381);
        item8 = new Item_item(0,"一盤肉 (99泰式燒烤火鍋)",702);
        item9 = new Item_item(0,"一盤菜 (99泰式燒烤火鍋)",121);
        item10 = new Item_item(0,"一碗湯 (99泰式燒烤火鍋)",433);
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

        item = new Item_item(0,"薯條-大 (麥當勞)",456);
        item2 = new Item_item(0,"聖代 (麥當勞)",312);
        item3 = new Item_item(0,"安格斯黑牛堡 (麥當勞)",416);
        item4 = new Item_item(0,"勁辣雞腿堡 (麥當勞)",490);
        item5 = new Item_item(0,"薯條-小 (麥當勞)",231);
        item6 = new Item_item(0,"冰炫風 (麥當勞)",336);
        item7 = new Item_item(0,"炸雞 (肯德基)",236);
        item8 = new Item_item(0,"雞米花-大 (肯德基)",183);
        item9 = new Item_item(0,"蛋塔 (肯德基)",182);
        item10 = new Item_item(0,"義式香草紙包雞 (肯德基)",386);
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

        item = new Item_item(0,"沙拉 (肯德基)",79);
        item2 = new Item_item(0,"玉米 (肯德基)",86);
        item3 = new Item_item(0,"脆薯-中 (肯德基)",185);
        item4 = new Item_item(0,"和風炸雞 (摩斯漢堡)",472);
        item5 = new Item_item(0,"抹茶紅豆千層派 (摩斯漢堡)",196);
        item6 = new Item_item(0,"法蘭克熱狗 (摩斯漢堡)",158);
        item7 = new Item_item(0,"紅茶 (摩斯漢堡)",109);
        item8 = new Item_item(0,"頻果汁 (摩斯漢堡)",102);
        item9 = new Item_item(0,"摩斯漢堡 (摩斯漢堡)",434);
        item10 = new Item_item(0,"脆薯-大 (摩斯漢堡)",446);
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
