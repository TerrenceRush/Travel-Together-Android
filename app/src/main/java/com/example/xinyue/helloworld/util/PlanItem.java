package com.example.xinyue.helloworld.util;

import android.net.Uri;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by xinyue on 5/18/15.
 */
public class PlanItem implements Serializable{
    private String id;
    private String name;
    private String holderId;
    private String title;
    private String destination;
    private String dateFrom;
    private int duration;
    private int groupSize;
    private int currentSize;
    private String description;
    private String avatar;

    public String getId() {
        return this.id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getHolderId(){
        return this.holderId;
    }

    public void setHolderId(String holderId){
        this.holderId = holderId;
    }

    public String getTitle(){
        return this.title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getDestination(){
        return this.destination;
    }

    public void setDestination(String destination){
        this.destination = destination;
    }

    public String getDateFrom(){
        return this.dateFrom;
    }

    public void setDateFrom(String dateFrom){
        this.dateFrom = dateFrom;
    }

    public int getDuration(){
        return this.duration;
    }

    public void setDuration(int duration){
        this.duration = duration;
    }

    public int getGroupSize(){
        return this.groupSize;
    }

    public void setGroupSize(int groupSize){
        this.groupSize = groupSize;
    }

    public int getCurrentSize(){
        return this.currentSize;
    }

    public void setCurrentSize(int currentSize){
        this.currentSize = currentSize;
    }

    public String getDescription(){
        return this.description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar){
        this.avatar = avatar;
    }
}
