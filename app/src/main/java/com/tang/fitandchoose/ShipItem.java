package com.tang.fitandchoose;
import java.text.DecimalFormat;

import java.util.Random;
/**
 * Created by trishcornez on 6/29/14.
 */
public class ShipItem {
    // DATA MEMBERS
    private int chinese_count;
    private int japan_count;
    private int italy_count;
    private int amercia_count;
    private int thiland_count;
    private int dessert_count;
    private int dislike_count;
    //private int restaurant_num;
    private Random ran = new Random();
    private String[] choose_restaurant;

    private String[] restaurant_1 = {"摩斯漢堡", "美式", "320桃園市中壢區環中東路266號1、2 樓","03 461 3484","星期一~星期日 06:00–23:00","@drawable/chinese_1_1","@drawable/chinese_1_2",
            "@drawable/chinese_1_3","@drawable/chinese_1_4","   @drawable/chinese_1_5", "@drawable/chinese_1_"};//
    private String[] restaurant_2 = {"興仁牛肉麵", "中式", "320桃園市中壢區興仁路二段71號","03 455 3902","星期日~星期五 11:00–20:00\n星期六公休","@drawable/chinese_2_1","@drawable/chinese_2_2",
            "@drawable/chinese_2_3","@drawable/chinese_2_4","@drawable/chinese_2_5", "@drawable/chinese_2_6"};//
    private String[] restaurant_3 = {"微笑的魚", "中式、泰式、日式", "320桃園市中壢區興安一街8巷6號","0930 369 81","星期二~星期日 11:30–23:29\n星期一公休","@drawable/chinese_3_1","@drawable/chinese_3_2",
            "@drawable/chinese_3_3","@drawable/chinese_3_4","@drawable/chinese_3_5", "@drawable/chinese_3_6"};//
    private String[] restaurant_4 = {"食之初料理", "日式", "330桃園市桃園區興安一街8巷10號","03 435 9239","星期一~星期五 11:00–14:00、17:00–20:00\n星期六、日公休","@drawable/japan_1_1","@drawable/japan_1_2",
            "@drawable/japan_1_3","@drawable/japan_1_4","@drawable/japan_1_5", "@drawable/japan_1_6"};//
    private String[] restaurant_5 = {"緣自壽司屋", "日式", "320桃園市中壢區興安一街8巷9 號","0908 927 110","星期一~星期日 11:00–21:30","@drawable/japan_2_1","@drawable/japan_2_2",
            "@drawable/japan_2_3","@drawable/japan_2_4","@drawable/japan_2_5", "@drawable/japan_2_6"};//
    private String[] restaurant_6 = {"麥當勞", "美式", "32071桃園市中壢區環中東路301號","03 455 9117","星期一~星期日 24 小時營業","@drawable/america_1_1","@drawable/america_1_2",
            "@drawable/america_1_3","@drawable/america_1_4","@drawable/america_1_5", "@drawable/america_1_6"};//
    private String[] restaurant_7 = {"放牛班", "美式", "320桃園市中壢區興仁路二段41號67巷","無","星期一~星期日 11:00–21:00","@drawable/america_2_1","@drawable/america_2_2",
            "@drawable/america_2_3","@drawable/america_2_4","@drawable/america_2_5", "@drawable/america_2_6"};//
    private String[] restaurant_8 = {"肯德基", "美式、義式、印式","320桃園市中壢區環中東路249、251號","03 455 9916","星期一~日 07:00–00:00","@drawable/america_3_1","@drawable/america_3_2",
            "@drawable/america_3_3","@drawable/america_3_4","@drawable/america_3_5", "@drawable/america_3_6"};//
    private String[] restaurant_9 = {"Pasta Good Good", "義式","320桃園市中壢區興仁路二段134號","0915 088 921","星期二~星期日 11:30–14:30、17:00–21:00\n星期日公休","@drawable/italy_1_1","@drawable/italy_1_2",
            "@drawable/italy_1_3","@drawable/italy_1_4","@drawable/italy_1_5", "@drawable/italy_1_6"};//
    private String[]  restaurant_10 = {"99泰式燒烤火鍋", "泰式","320桃園市中壢區興仁路二段136號","03 434 5799","星期一~星期五 16:30–20:30\n星期六~星期日 11:30–22:30","@drawable/thailand_1_1","@drawable/thailand_1_2",
            "@drawable/thailand_1_3","@drawable/thailand_1_4","@drawable/thailand_1_5", "@drawable/thailand_1_6"};//
    private String[]  restaurant_11 = {"元智咖哩匠", "印式","320桃園市中壢區興仁路二段67巷16號","03 285 3221","星期一~星期日 11:00–15:00 16:30–20:30","@drawable/thailand_2_1","@drawable/thailand_2_2",
            "@drawable/thailand_2_3","@drawable/thailand_2_4","@drawable/thailand_2_5", "@drawable/thailand_2_6"};//
    private String[] restaurant_12 = {"破舍咖啡", "甜點", "桃園市中壢區興仁路一段168號",":0925191225","星期一~星期日 12:00~22:00","@drawable/dessert_3_1","@drawable/dessert_3_2",
            "@drawable/dessert_3_3","@drawable/dessert_3_4","@drawable/dessert_3_5", "@drawable/dessert_3_6"};//

    public ShipItem() {//
        chinese_count = 0;
        japan_count = 0;
        italy_count = 0;
        amercia_count = 0;
        thiland_count = 0;
        dessert_count = 0;
        dislike_count = 0;
    }

    public void reset(){
        chinese_count = 0;
        japan_count = 0;
        italy_count = 0;
        amercia_count = 0;
        thiland_count = 0;
        dessert_count = 0;
        dislike_count = 0;
    }

    // SETTING FUNCTIONS
    public void increase_chinese_count() { chinese_count++; }

    public void increase_japan_count() { japan_count++; }

    public void increase_italy_count() { italy_count++; }

    public void increase_amercia_count() { amercia_count++; }

    public void increase_thiland_count() { thiland_count++; }

    public void increase_dessert_count() { dessert_count++; }

    public void increase_dislike_count() { dislike_count++; }

    public void dislike_reset() { dislike_count = 0; }

    //GET FUNCTIONS

    public int get_like_count(){
        int max = chinese_count;
        if(max < japan_count)
            max = japan_count;
        if(max < italy_count)
            max = italy_count;
        if(max < thiland_count)
            max = thiland_count;
        if(max < amercia_count)
            max = amercia_count;
        if(max < dessert_count)
            max = dessert_count;
        return max;
    }

    public int get_dislike_count(){ return dislike_count; }

    public String[] get_restaurant_1(){ return restaurant_1; }
    public String[] get_restaurant_2(){ return restaurant_2; }
    public String[] get_restaurant_3(){ return restaurant_3; }
    public String[] get_restaurant_4(){ return restaurant_4; }
    public String[] get_restaurant_5(){ return restaurant_5; }
    public String[] get_restaurant_6(){ return restaurant_6; }
    public String[] get_restaurant_7(){ return restaurant_7; }
    public String[] get_restaurant_8(){ return restaurant_8; }
    public String[] get_restaurant_9(){ return restaurant_9; }
    public String[] get_restaurant_10(){ return restaurant_10; }
    public String[] get_restaurant_11(){ return restaurant_11; }
    public String[] get_restaurant_12(){ return restaurant_12; }

    public String[] random_restaurant() {
        int restaurant_num = ran.nextInt(11);
        switch (restaurant_num){
            case 0:
                choose_restaurant = restaurant_1;
                break;
            case 1:
                choose_restaurant = restaurant_2;
                break;
            case 2:
                choose_restaurant = restaurant_3;
                break;
            case 3:
                choose_restaurant = restaurant_4;
                break;
            case 4:
                choose_restaurant = restaurant_5;
                break;
            case 5:
                choose_restaurant = restaurant_6;
                break;
            case 6:
                choose_restaurant = restaurant_7;
                break;
            case 7:
                choose_restaurant = restaurant_8;
                break;
            case 8:
                choose_restaurant = restaurant_9;
                break;
            case 9:
                choose_restaurant = restaurant_10;
                break;
            case 10:
                choose_restaurant = restaurant_11;
                break;
            default:
                choose_restaurant = restaurant_12;
                break;
        }
        return choose_restaurant;
    }
}

