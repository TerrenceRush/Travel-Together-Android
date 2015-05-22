package com.example.xinyue.helloworld.Network;

import org.apache.http.client.methods.HttpPost;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by xinyue on 5/18/15.
 */
public class ConnNet {

    public static final String apiHOST = "http://cloud6998.elasticbeanstalk.com/v1/";

    public HttpURLConnection getPostConn(String urlPath) {
        String finalUrl = apiHOST + urlPath;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(finalUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return connection;

    }

    public HttpURLConnection getGetConn(String urlPath){
        String finalUrl = apiHOST + urlPath;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(finalUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return connection;
    }
}
