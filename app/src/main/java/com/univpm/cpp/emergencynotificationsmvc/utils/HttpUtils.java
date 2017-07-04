package com.univpm.cpp.emergencynotificationsmvc.utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.univpm.cpp.emergencynotificationsmvc.models.Jsonable;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Classe che gestisce le connessoni
 */
public class HttpUtils {

    private Broadcaster broadcaster;
    private static final String SERVER_HOST = "90.147.44.150:8080"; //Host del server
    private static final String SERVER_NAME = "EmergencyNotificationsServer"; //Nome del server

    public static final String INTENT_CONNECTION_REFUSED = "com.univpm.cpp.emergencynotificationsmvc.CONNECTION_REFUSED";

    public HttpUtils(Broadcaster broadcaster){
        this.broadcaster = broadcaster;
    }

    // HTTP GET request
   public String sendGet(String url)  {
       String responseString = null;
       try {

           URL obj = new URL("http://" + SERVER_HOST + "/" + SERVER_NAME + "/webresources/" + url);
           //Log.w("Url_GET", "http://" + SERVER_HOST + "/" + SERVER_NAME + "/webresources/" + url);
           java.net.HttpURLConnection con = (java.net.HttpURLConnection) obj.openConnection();

           // optional default is GET
           con.setRequestMethod("GET");

           //add request header
           con.setRequestProperty("Content-Type", "application/json");
           con.setRequestProperty("Accept", "application/json");


           int responseCode = con.getResponseCode();

           //Log.w("Responsecode:", String.valueOf(responseCode));

           if (responseCode == HttpURLConnection.HTTP_OK) {
               BufferedReader in = new BufferedReader(
                       new InputStreamReader(con.getInputStream()));
               String inputLine;
               StringBuffer response = new StringBuffer();

               while ((inputLine = in.readLine()) != null) {
                   response.append(inputLine);
               }
               in.close();

               responseString = response.toString();

               //Log.w("ResponseToGetRequest:", response.toString());

           }
       } catch (Exception e){
           e.printStackTrace();
           handleException(e);
           return null;
       }

        return responseString;
    }

    // HTTP POST request
    public boolean sendPost(String url, Jsonable obj){

        int responseCode = -1;

        try {
            //Log.w("Url_POST", "http://" + SERVER_HOST + "/" + SERVER_NAME + "/webresources/" + url);
            URL urlObj = new URL("http://" + SERVER_HOST + "/" + SERVER_NAME + "/webresources/" + url);
            HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

            //add reuqest header
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");

            // Send post request
            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            //Log.w("JsonString_PostRequest", obj.toJson().toString());
            wr.write(obj.toJson().toString());
            wr.flush();


            responseCode = con.getResponseCode();

        } catch (Exception e){
            e.printStackTrace();
            handleException(e);
            return false;
        }

        //Log.w("Response_PostRequest", String.valueOf(responseCode));
       return (responseCode == HttpURLConnection.HTTP_NO_CONTENT);
    }

    // HTTP PUT request
    public boolean sendPut(String url, Jsonable obj){
        int responseCode = -1;

        try {
            //Log.w("Url_PUT", "http://" + SERVER_HOST + "/" + SERVER_NAME + "/webresources/" + url);
            URL urlObj = new URL("http://" + SERVER_HOST + "/" + SERVER_NAME + "/webresources/" + url);
            HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

            //add request header
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");

            // Send put request
            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            //Log.w("JsonString", obj.toJson().toString());
            wr.write(obj.toJson().toString());
            wr.flush();

            responseCode = con.getResponseCode();
        } catch (Exception e){
            e.printStackTrace();
            handleException(e);
            return false;
        }

        //Log.w("Response", String.valueOf(responseCode));
        return (responseCode == HttpURLConnection.HTTP_NO_CONTENT);
    }


    public Bitmap getMapBitmap(String mapname) {

        Bitmap img = null;

        try {
            URL urlObj = new URL("http://" + SERVER_HOST + "/" + SERVER_NAME + "/webresources/map/" + mapname + "/png");
            HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();

            //add request header
            String fileUrl = Directories.MAPS + File.separator + mapname + ".png";
            //Log.w("FileUrl", fileUrl);
            File imageFile = new File(fileUrl);
            if (imageFile.exists()) {
                Long lastModified = imageFile.lastModified();
                Date lastModifiedDate = new Date(lastModified);
                //Log.w("DataModifica: ", lastModifiedDate.toString());

                SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy", Locale.ENGLISH);
                String parsedDate = formatter.format(lastModifiedDate);

                //Log.w("DataModificaPars", parsedDate);

                connection.setRequestProperty("If-Modified-Since", parsedDate);
            }

            InputStream is = connection.getInputStream();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                img = BitmapFactory.decodeStream(is);
            } else if (connection.getResponseCode() == HttpURLConnection.HTTP_NOT_MODIFIED) {
                //Log.w("Image:", "not-modified");
            }
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return img;
    }

    public static boolean serverOn()  {
        URL urlObj = null;
        try {
            urlObj = new URL("http://" + SERVER_HOST + "/" + SERVER_NAME + "/webresources/map");
            HttpURLConnection con  = (HttpURLConnection) urlObj.openConnection();
            con.setRequestMethod("GET");

            con.setConnectTimeout(2000);

            //add request header
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");

            con.getResponseCode();

            //Log.w("Connessione", "ok");
        } catch (IOException e) {
            String message = e.getMessage();
            Log.w("Connessione", e.getMessage());
            return false;
        }
        return true;
    }

    private void handleException(Exception e){
        switch (e.getMessage()){
            case "Connection refused":
            case "No route to host":
                Intent intnet = new Intent(INTENT_CONNECTION_REFUSED);
                broadcaster.sendBroadcast(intnet);
                break;
        }
    }
}
