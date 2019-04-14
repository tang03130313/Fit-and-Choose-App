package com.tang.fitandchoose.item;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Item_sport implements Serializable{

    private long id;
    private String name;
    private long kcal;

    public Item_sport(){}

    public Item_sport(long id, String name, long kcal){
        this.id=id;
        this.name=name;
        this.kcal=kcal;
    }
    public void setId(long id){
        this.id=id;
    }
    public long getId(){
        return this.id;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getName(){
        return this.name;
    }
    public void setKcal(long kcal){
        this.kcal=kcal;
    }
    public long getKcal(){
        return this.kcal;
    }
}
