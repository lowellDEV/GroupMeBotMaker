package comlowelldev.github.groupmebotmaker;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Rider X2
 */
import android.os.StrictMode;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.*;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.*;

public class HTTP {
    /*Temporary for test*/
    private static StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    /**************************************************/

    public HTTP() {

    }
    /**
     * Send a post request 
     * @param urlString the url to send to
     * @param params the variables to add to the url
     * @param data jsonString representing the data
     * @return InputStream containing response
     */
    public static InputStream SendPOST(String urlString, JSONObject params, String data) throws JSONException {
        URL url;
//        System.out.println(urlString);
//        System.out.println(data);
//        System.out.println("Parameters: "+params.toString());
        if (params != null) {
            urlString +="?";
            Iterator<?> keys = params.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                String value = (String) params.getString(key);
                urlString+= key+"="+value;
            }
            
        }
        try {
            url = new URL(urlString);
            System.out.println("Query: "+urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            /*
            Iterator<?> keys = params.keys();
            while (keys.hasNext()){
                String  key =(String)keys.next();
                connection.setRequestProperty(key, (String)params.getString(key));
            }*/

            connection.getOutputStream().write(data.getBytes("UTF-8"));
            connection.getOutputStream().flush();
            if (connection.getResponseCode() >= 200 && connection.getResponseCode() < 300) {
                return connection.getInputStream();
            }
            else{
                System.out.println(connection.getResponseMessage());
            }
        } catch (MalformedURLException e) {
            System.out.println("Malformed URL");
        } catch (IOException ex) {
            Logger.getLogger(HTTP.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("IO Exception");
        }

        return null;
    }
    /**
     * Send a GET request
     * @param urlString the url to send the request to
     * @param params the variables to add to the url
     * @return InputStream containing response
     */
    public static InputStream sendGET(String urlString, JSONObject params) throws JSONException {
        /*Temporary for test*/
        StrictMode.setThreadPolicy(policy);
        /*****************************/
        URL url;
        if (params != null) {
            urlString +="?";
            Iterator<?> keys = params.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                String value = (String) params.getString(key);
                urlString+= key+"="+value;
            }
            
        }
        try {
            url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            /*
            Iterator<?> keys = params.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                connection.setRequestProperty(key, (String) params.getString(key));
            }*/
            //System.out.println(url.getQuery());
            if (connection.getResponseCode() >= 200 && connection.getResponseCode() < 300) {
                return connection.getInputStream();
            }
        } catch (MalformedURLException e) {
            System.out.println("Malformed URL");
        } catch (IOException ex) {
            Logger.getLogger(HTTP.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}
