package com.crash.etranzact.masterpass;
/**
 * Created by Etranzact on 10/10/2017.
 */
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
public class HttpConnectionUtil {
    public String getURLConnection(String url, String urlParameters) {
        StringBuffer response = new StringBuffer();
        System.out.println(url+ " XXXXXXXXXXXXXXXXX "+urlParameters);
        String USER_AGENT = "Mozilla/5.0";
        try {

            URL urlObject = new URL(url);
            try {
                HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestProperty("User-Agent", USER_AGENT);
                connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

                // Send post request
                //connection.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();
                int responseCode = connection.getResponseCode();
                Log.i("Sending ", url);
                Log.i("Post parameters", urlParameters);
                Log.i("Response Code", responseCode+"");

                BufferedReader in;

                if(responseCode == 200){
                    in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                }else{
                    in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                }


                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                Log.i(" response.toString() ", response.toString());
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
}
