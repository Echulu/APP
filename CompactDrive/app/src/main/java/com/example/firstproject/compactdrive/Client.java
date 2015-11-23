package com.example.firstproject.compactdrive;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class Client {

    private static String clientId= "590541311833-ur79hjs509vp74l9c2vq76e8c3mk26p3.apps.googleusercontent.com";
    private static String redirect_uri = "urn:ietf:wg:oauth:2.0:oob";
    public  static String aceToken="";
    private static String refToken="";
    public static String CODE=null;
    private static StringBuffer fileList;

    public static String generatePermissionUrl() {

        String response_type = "code";
        String approval_prompt = "force";
        String access_type = "offline";
        String scope = "https://www.googleapis.com/auth/drive.metadata";
        String endPoint = "https://accounts.google.com/o/oauth2/auth";

        String finalUrl = endPoint + "?" + "client_id=" + clientId + "&response_type=" + response_type
                + "&approval_prompt=" + approval_prompt + "&access_type=" + access_type + "&scope=" + scope
                + "&redirect_uri=" + redirect_uri;
        return finalUrl;
    }

    public static void populateTokens() {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                String endPoint = "https://accounts.google.com/o/oauth2/token";
                StringBuffer response = new StringBuffer();
                try {
                    String body_string ="grant_type=authorization_code"+"&code="+CODE+"&client_id="+clientId+"&redirect_uri="+redirect_uri;
                    URL temp = new URL(endPoint);
                    HttpURLConnection con = (HttpURLConnection) temp.openConnection();
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
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
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void refreshToken() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                String endPoint = "https://accounts.google.com/o/oauth2/token";
                StringBuffer response = new StringBuffer();
                try {
                    String body_string ="grant_type=refresh_token&client_id="+clientId+"refresh_token"+refToken+"&redirect_uri="+redirect_uri;
                    URL temp = new URL(endPoint);
                    HttpURLConnection con = (HttpURLConnection) temp.openConnection();
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
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
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static JSONObject getAll() {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                 fileList = new StringBuffer();
                String resource = "https://www.googleapis.com/drive/v2/files/root";
                try {
                    URL temp = new URL(resource);
                    HttpURLConnection con = (HttpURLConnection) temp.openConnection();
                    Thread.sleep(1000);
                    con.setRequestMethod("GET");
                    String authToken = "OAuth " +aceToken;
                    con.setRequestProperty("Authorization",authToken);
                    int c = con.getResponseCode();
                    if (con.getResponseCode() == 200) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        String inputLine;
                        while ((inputLine = in.readLine()) != null) {
                            fileList.append(inputLine);
                        }
                    } else if(c==403){
                        Client.refreshToken();
                        Client.getAll();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        t.start();

        try {
            t.join();
            return new JSONObject("fileList.toString()");
        }catch (Exception e){
        }
        return null;
    }
}