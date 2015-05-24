package com.example.xinyue.helloworld.util;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by qshen on 5/23/15.
 */
public class PlanGenerator {
    private PlanGenerator(){
        super();
    }

    public static String getPlanString(String title, String des, String depart, String length, String descpt, String type, String limit, List<String> friends){
        JSONObject json = new JSONObject();
        StringBuilder string = new StringBuilder("{\n");
        string.append("'title':'" + title+"',\n");
        string.append("'destination':'" + des + "',\n");
        string.append("'depart_time':" + depart + ",\n");
        string.append("'length':" + length + ",\n");
        string.append("'description':\"" + descpt + "\",\n");
        string.append("'type':" + type + ",\n");
        string.append("'limit':" + limit + ",\n");
        string.append("'friendlist':[");
        if(!friends.isEmpty()){
            String first = friends.get(0);
            string.append("'" + first + "'");
        }
        for(int i=1; i<friends.size(); i++){
            string.append(",'" + friends.get(i) + "'");
        }
        string.append("]\n}");
        return string.toString();
    }
}
