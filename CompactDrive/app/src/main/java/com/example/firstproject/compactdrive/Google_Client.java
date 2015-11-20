package com.example.firstproject.compactdrive;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.JsonToken;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class Google_Client {

    private static String clientId= "838886148448-79ocussphq9dscrbsgb81h516oev5npg.apps.googleusercontent.com";
    private static String redirect_uri = "urn:ietf:wg:oauth:2.0:oob";
    public  static String aceToken="";
    private static String refToken="";
    private static String codeInput="";

    public static String generatePermissionUrl() {

        String response_type = "code";
        String approval_prompt = "force";
        String access_type = "offline";
        String scope = "https://www.googleapis.com/auth/drive";
        String endPoint = "https://accounts.google.com/o/oauth2/auth";

        String finalUrl = endPoint + "?" + "client_id=" + clientId + "&response_type=" + response_type
                + "&approval_prompt=" + approval_prompt + "&access_type=" + access_type + "&scope=" + scope
                + "&redirect_uri=" + redirect_uri;
        return finalUrl;
    }

    public String populateTokens(String code) {
        codeInput = code;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                String endPoint = "https://accounts.google.com/o/oauth2/token";
                StringBuffer response = new StringBuffer();
                try {
                    String body_string ="grant_type=authorization_code"+"&code="+codeInput+"&client_id="+clientId+"&redirect_uri="+redirect_uri;
                    URL temp = new URL(endPoint);
                    HttpURLConnection con = (HttpURLConnection) temp.openConnection();
                    con.setRequestMethod("POST");
                    //DataOutputStream token_stream = new DataOutputStream(con.getOutputStream());
                    OutputStream token_stream = con.getOutputStream();
                    token_stream.write(body_string.getBytes());
                    token_stream.flush();
                    token_stream.close();
                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    JSONObject o = new JSONObject(response.toString());
                    aceToken = o.getString("access_token");
                    refToken = o.getString("refresh_token");
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return aceToken;
    }
    public String refreshToken(String code) {
        codeInput = code;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                String endPoint = "https://accounts.google.com/o/oauth2/token";
                StringBuffer response = new StringBuffer();
                try {
                    String body_string ="grant_type=refresh_token"+"&code="+codeInput+"&client_id="+clientId+"refresh_token"+refToken+"&redirect_uri="+redirect_uri;
                    URL temp = new URL(endPoint);
                    HttpURLConnection con = (HttpURLConnection) temp.openConnection();
                    con.setRequestMethod("POST");
                    //DataOutputStream token_stream = new DataOutputStream(con.getOutputStream());
                    OutputStream token_stream = con.getOutputStream();
                    token_stream.write(body_string.getBytes());
                    token_stream.flush();
                    token_stream.close();
                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    JSONObject o = new JSONObject(response.toString());
                    aceToken = o.getString("access_token");
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return aceToken;
    }
}
