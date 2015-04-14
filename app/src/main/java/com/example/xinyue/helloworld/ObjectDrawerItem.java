package com.example.xinyue.helloworld;

/**
 * Created by xinyue on 4/14/15.
 */
public class ObjectDrawerItem {

    private String title;
    private int icon;

    public ObjectDrawerItem(int icon, String title){
        this.title = title;
        this.icon = icon;
    }

    public String getTitle(){
        return this.title;
    }

    public int getIcon(){
        return this.icon;
    }
}


