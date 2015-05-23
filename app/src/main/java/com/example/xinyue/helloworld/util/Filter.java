package com.example.xinyue.helloworld.util;

/**
 * Created by xinyue on 5/22/15.
 */
public class Filter {
    private int maxGroupSize;
    private String destination;
    private int startDateYear;
    private int startDateMonth;
    private int startDateDay;

    public Filter(int maxGroupSize, String destination, int startDateYear, int startDateMonth, int startDateDay){
        this.destination = destination;
        this.maxGroupSize = maxGroupSize;
        this.startDateMonth = startDateMonth;
        this.startDateDay = startDateDay;
        this.startDateYear = startDateYear;
    }

    public boolean ifMatch(PlanItem planItem){
        if(!planItem.getDestination().toLowerCase().contains(destination.toLowerCase())){
            return false;
        }
        if(planItem.getGroupSize()>maxGroupSize){
            return false;
        }
        int year = Integer.parseInt(planItem.getDateFrom().split("-")[0]);
        int month = Integer.parseInt(planItem.getDateFrom().split("-")[1]);
        int day = Integer.parseInt(planItem.getDateFrom().split("-")[2]);
        if(year>startDateYear){
            return false;
        }
        if(month>startDateMonth){
            return false;
        }
        if(day>startDateDay){
            return false;
        }
        return true;
    }

}
