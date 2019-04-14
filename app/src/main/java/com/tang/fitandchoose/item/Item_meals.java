package com.tang.fitandchoose.item;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Item_meals implements Serializable{

    private long id;
    private String name;
    private long number;

    public Item_meals(){}

    public Item_meals(long id, String name){
        this.id=id;
        this.name=name;
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
    public void setNumber(long number){
        this.number=number;
    }
    public long getNumber(){
        return this.number;
    }
}
