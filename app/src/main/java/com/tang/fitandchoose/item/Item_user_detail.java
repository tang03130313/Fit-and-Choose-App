package com.tang.fitandchoose.item;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Item_user_detail implements Serializable{

    private long id;
    private String user;
    private String date;
    private String category;
    private String item;
    private long kcal;
    private long number;

    public Item_user_detail(){}

    public Item_user_detail(long id, String user, String date, String category, String item, long kcal, long number){
        this.id=id;
        this.user=user;
        this.date=date;
        this.category=category;
        this.item=item;
        this.kcal=kcal;
        this.number=number;
    }
    public void setId(long id){
        this.id=id;
    }
    public long getId(){
        return this.id;
    }
    public void setUser(String user){
        this.user=user;
    }
    public String getUser(){
        return this.user;
    }
    public void setDate(String date){
        this.date=date;
    }
    public String getDate(){
        return this.date;
    }
    public void setCategory(String category){
        this.category=category;
    }
    public String getCategory(){
        return this.category;
    }
    public void setItem(String item){
        this.item=item;
    }
    public String getItem(){
        return this.item;
    }
    public void setKcal(long kcal){
        this.kcal=kcal;
    }
    public long getKcal(){
        return this.kcal;
    }
    public void setNumber(long number){
        this.number=number;
    }
    public long getNumber(){
        return this.number;
    }
}
