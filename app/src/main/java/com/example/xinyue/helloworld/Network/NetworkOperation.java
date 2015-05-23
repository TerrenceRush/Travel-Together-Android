package com.example.xinyue.helloworld.Network;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

/**
 * Created by xinyue on 5/18/15.
 */
public class NetworkOperation{
    public JSONObject getPlanList(String accessToken, String type){
        ConnNet connNet = new ConnNet();
        String url = "plan/"+accessToken+"/"+type;
        HttpURLConnection conn = connNet.getGetConn(url);
        try {
            conn.connect();
            int responseCode = conn.getResponseCode();
            Log.d("RESPONSE CODE",  Integer.toString(responseCode));
            if(responseCode == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                return new JSONObject(sb.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return null;
    }

    public JSONObject planActions(String action, String accessToken, String planid) {
        ConnNet connNet = new ConnNet();
        String url = action +"/"+accessToken+"/"+planid;
        HttpURLConnection conn;
        if (action.equals("delete")) {
            conn = connNet.getDeleteConn(url);
        }
        else {
            conn = connNet.getPostConn(url);
        }
        try {
            conn.connect();
            int responseCode = conn.getResponseCode();
            Log.d("RESPONSE CODE",  Integer.toString(responseCode));
            if(responseCode == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                return new JSONObject(sb.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return null;

    }


    public JSONObject authenticate(String accessToken, String data){
        ConnNet connNet = new ConnNet();
        String url = "auth/"+accessToken;
        HttpURLConnection conn = connNet.getPostConn(url);
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        OutputStream os = null;

        try {
            conn.connect();
            os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(data);
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();
            if(responseCode == 200){
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                return new JSONObject(sb.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }



}
