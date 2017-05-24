package com.univpm.cpp.emergencynotificationsmvc.utils;

import android.util.Log;

import com.univpm.cpp.emergencynotificationsmvc.models.Jsonable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {

    private static final String SERVER_HOST = "172.23.170.169:8080";
    private static final String SERVER_NAME = "EmergencyNotifications";


    // HTTP GET request
   public static String sendGet(String url) throws Exception {

        URL obj = new URL("http://" + SERVER_HOST + "/" + SERVER_NAME + "/webresources/" + url);
        java.net.HttpURLConnection con = (java.net.HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");


        int responseCode = con.getResponseCode();
        String responseString = null;

        if (responseCode == HttpURLConnection.HTTP_OK){
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            responseString = response.toString();

            //print result
            Log.w("Response:" , response.toString());

        }

        return responseString;
    }

    // HTTP POST request
    public static boolean sendPost(String url, Jsonable obj) throws Exception {

        Log.w("Url", "http://" + SERVER_HOST + "/" + SERVER_NAME + "/webresources/" + url);
        URL urlObj = new URL("http://" + SERVER_HOST + "/" + SERVER_NAME + "/webresources/" + url);
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");

        // Send post request
        OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
        Log.w("JsonString", obj.toJson().toString());
        wr.write(obj.toJson().toString());
        wr.flush();


        int responseCode = con.getResponseCode();

        /*
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        Log.w("Response:" , response.toString());
        */

        Log.w("Response", String.valueOf(responseCode));
       return (responseCode == HttpURLConnection.HTTP_NO_CONTENT);
    }

    // HTTP PUT request
    public static boolean sendPut(String url, Jsonable obj) throws Exception {

        Log.w("Url", "http://" + SERVER_HOST + "/" + SERVER_NAME + "/webresources/" + url);
        URL urlObj = new URL("http://" + SERVER_HOST + "/" + SERVER_NAME + "/webresources/" + url);
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

        //add request header
        con.setRequestMethod("PUT");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");

        // Send put request
        OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
        Log.w("JsonString", obj.toJson().toString());
        wr.write(obj.toJson().toString());
        wr.flush();

        int responseCode = con.getResponseCode();

        /*
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        Log.w("Response:" , response.toString());
        */

        Log.w("Response", String.valueOf(responseCode));
        return (responseCode == HttpURLConnection.HTTP_NO_CONTENT);
    }
}
