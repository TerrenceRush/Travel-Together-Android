package com.example.xinyue.helloworld.Network;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    public JSONObject authenticate(String accessToken){
        ConnNet connNet = new ConnNet();
        String url = "auth/"+accessToken;
        HttpURLConnection conn = connNet.getGetConn(url);
        try {
            conn.connect();
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
