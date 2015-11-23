package com.example.firstproject.compactdrive;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class Gmail_Auth extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmail__auth);
        new Gmail().execute();
    }
    public class Gmail extends AsyncTask {

        private StringBuffer fileList = new StringBuffer();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Client.populateTokens();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            if (Client.aceToken != null) {
                try {
                    URL temp = new URL("https://www.googleapis.com/drive/v2/files/root");
                    HttpURLConnection con = (HttpURLConnection) temp.openConnection();
                    con.setRequestMethod("GET");
                    String authToken = "OAuth " + Client.aceToken;
                    con.setRequestProperty("Authorization", authToken);
                    int c = con.getResponseCode();
                    if (con.getResponseCode() == 200) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        String inputLine;
                        while ((inputLine = in.readLine()) != null) {
                            fileList.append(inputLine);
                        }
                    } else if (c == 403) {
                        Client.refreshToken();
                        Client.getAll();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            JSONObject j;
            super.onPostExecute(o);
            String k ="DEMO";
            try {
               j= new JSONObject(fileList.toString());
                k=j.getString("kind");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ListView files = (ListView)findViewById(R.id.fileview);
            while(){

            }
        }
    }
}