package com.example.xinyue.helloworld.util;

/**
 * Created by xinyue on 5/22/15.
 */
public class Filter {
    private String destination;
    private int startDateYear;
    private int startDateMonth;
    private int startDateDay;

    public Filter(String destination, int startDateYear, int startDateMonth, int startDateDay){
        this.destination = destination;
        this.startDateMonth = startDateMonth;
        this.startDateDay = startDateDay;
        this.startDateYear = startDateYear;
    }

    public boolean ifMatch(PlanItem planItem){
        if(!planItem.getDestination().toLowerCase().contains(this.destination.toLowerCase())){
            return false;
        }
        String startDate = planItem.getDateFrom().split(" ")[0];
        int year = Integer.parseInt(startDate.split("-")[0]);
        int month = Integer.parseInt(startDate.split("-")[1]);
        int day = Integer.parseInt(startDate.split("-")[2]);
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
