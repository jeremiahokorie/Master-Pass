package com.crash.etranzact.masterpass;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Joseph.Akinnufesi on 10/13/2017.
 */

public class token_getter {
    public String getURLConnection(String url, String auth_token) {
        StringBuffer respon = new StringBuffer();
        System.out.println(url + " XXXXXXXXXXXXXXXXX auth_token: " + auth_token);
        String USER_AGENT = "Mozilla/5.0";
        try {

            URL urlObject = new URL(url);
            try {
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("User-Agent", USER_AGENT);
                con.setRequestProperty("auth_token", auth_token);
try {
    int responseCode = con.getResponseCode();
    System.out.println("\nSending 'GET' request to URL : " + url);
    System.out.println("Response Code : " + responseCode);

}catch (Exception e){

}
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //print result
                System.out.println(response.toString());
                return response.toString();

            } catch (Exception sdz) {
                sdz.printStackTrace();
                return "";
            }

        } catch (Exception sd) {

            //sd.printStackTrace();
            return "";
        }

    }

    public String postURLConnection(String url, String auth_token, String jsonString) {
        StringBuffer respon = new StringBuffer();
        System.out.println(url + " XXXXXXXXXXXXXXXXX auth_token: " + auth_token+"  jsonString "+jsonString);
        String USER_AGENT = "Mozilla/5.0";
        try {

            URL urlObject = new URL(url);
            try {
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                con.setRequestProperty("User-Agent", USER_AGENT);
                con.setRequestProperty("Content-type", "application/json");
                con.setRequestProperty("auth_token", auth_token);

                con.setDoInput(true);
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(jsonString);
                int responseCode = con.getResponseCode();
                System.out.println("\nSending 'GET' request to URL : " + url);
                System.out.println("Response Code : " + responseCode);

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //print result
                System.out.println(response.toString());
                return response.toString();

            } catch (Exception sdz) {
                sdz.printStackTrace();
                return "";
            }

        } catch (Exception sd) {

            sd.printStackTrace();
            return "";
        }

    }
}

