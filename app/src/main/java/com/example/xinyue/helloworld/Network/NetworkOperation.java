package com.example.xinyue.helloworld.Network;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
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

    public JSONObject addPlan(String accessToken, String query){
        ConnNet connNet = new ConnNet();
        OutputStream os = null;

        try{
            HttpURLConnection connection = connNet.getPostConn("add/"+accessToken);
            Log.i("url", query);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.connect();
            os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.close();
            os.close();
            int responseCode = connection.getResponseCode();
            Log.i("responseCode", Integer.toString(responseCode));
            if(responseCode == 200){
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                Log.i("res", sb.toString());
                return new JSONObject(sb.toString());
            }
        }
        catch (Exception e) {

            e.printStackTrace();
            return null;
        }
        return null;
    }



}
