package com.tang.fitandchoose.item;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Item_num implements Serializable{

    private long id;
    private String name;
    private long number;

    public Item_num(){}

    public Item_num(long id, long number){
        this.id=id;
        this.number=number;
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
