package com.univpm.cpp.emergencynotificationsmvc.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.univpm.cpp.emergencynotificationsmvc.models.Jsonable;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HttpUtils {

    private static final String SERVER_HOST = "172.23.170.169:8080";
    private static final String SERVER_NAME = "EmergencyNotificationsServer";

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

       Log.w("Responsecode:" , String.valueOf(responseCode));

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
            Log.w("ResponseToGetRequest:" , response.toString());

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
        Log.w("JsonString_PostRequest", obj.toJson().toString());
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

        Log.w("Response_PostRequest", String.valueOf(responseCode));
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


    public static Bitmap getMapBitmap(String mapname) throws Exception {

        URL urlObj = new URL("http://" + SERVER_HOST + "/" + SERVER_NAME + "/webresources/map/" + mapname + "/png");
        HttpURLConnection connection  = (HttpURLConnection) urlObj.openConnection();

        //add request header
        String fileUrl = Directories.MAPS + File.separator + mapname + ".png";
        Log.w("FileUrl", fileUrl);
        File imageFile = new File(fileUrl);
        if (imageFile.exists()) {
            Long lastModified = imageFile.lastModified();
            Date lastModifiedDate = new Date(lastModified);
            Log.w("DataModifica: ", lastModifiedDate.toString());

            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy", Locale.ENGLISH);
            String parsedDate = formatter.format(lastModifiedDate);

            Log.w("DataModificaPars", parsedDate);

            connection.setRequestProperty("If-Modified-Since", parsedDate);
        }

        InputStream is = connection.getInputStream();
        Bitmap img = null;
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            img = BitmapFactory.decodeStream(is);
        } else if (connection.getResponseCode() == HttpURLConnection.HTTP_NOT_MODIFIED) {
            Log.w("Image:", "not-modified");
        }
        return img;
    }
}
