package com.example.xinyue.helloworld.util;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by qshen on 5/23/15.
 */
public class PlanGenerator {
    private PlanGenerator(){
        super();
    }

    public static String getPlanString(String title, String des, String depart, int length, String descpt, int type, int limit, List<String> friends){
        StringBuilder string = new StringBuilder();
        string.append("title=");
        try {
            string.append(URLEncoder.encode(title, "utf-8"));
            string.append("&destination=");
            string.append(URLEncoder.encode(des, "utf-8"));
            string.append("&depart_time=");
            string.append(URLEncoder.encode(depart, "utf-8"));
            string.append("&length=");
            string.append(URLEncoder.encode(Integer.toString(length), "utf-8"));
            string.append("&description=");
            string.append(URLEncoder.encode(descpt, "utf-8"));
            string.append("&type=");
            string.append(URLEncoder.encode(Integer.toString(type),"utf-8"));
            string.append("&limit=");
            string.append(URLEncoder.encode(Integer.toString(limit), "utf-8"));
            string.append("&friendlist=");
            string.append(URLEncoder.encode(friends.toString(), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return string.toString();
//        List<NameValuePair> params = new ArrayList<NameValuePair>();
//        params.add(new BasicNameValuePair("title", title));
//        params.add(new BasicNameValuePair("destination", des));
//        params.add(new BasicNameValuePair("depart", depart));
//        params.add(new BasicNameValuePair("length", Integer.toString(length)));
//        params.add(new BasicNameValuePair("description", descpt));
//        params.add(new BasicNameValuePair("type", Integer.toString(type)));
//        params.add(new BasicNameValuePair("limit", Integer.toString(limit)));
//        params.add(new BasicNameValuePair("friendlist", friends.toString()));
//        return params;

//        JSONObject json = new JSONObject();
//        try {
//            json.put("title", title);
//            json.put("destination", des);
//            json.put("depart_time", depart);
//            json.put("length", length);
//            json.put("description", descpt);
//            json.put("type", type);
//            json.put("limit", limit);
//            JSONArray list = new JSONArray();
//            for(String name : friends)
//                list.put(name);
//            json.put("friendlist", list);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return json.toString();
    }
}
