package com.example.firstproject.compactdrive;

import android.app.Activity;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


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
                    URL temp = new URL("https://www.googleapis.com/drive/v2/files");
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
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            JSONArray k = null;
            JSONObject p = null;
            ArrayList al = new ArrayList();
            try {
                JSONObject j = new JSONObject(fileList.toString());

                k = new JSONArray();
                k = (JSONArray)j.get("items");
                int len = k.length();

                while(len -- > 0) {
                    p = (JSONObject) k.get(len);
                    al.add(p.getString("title"));
                    Log.i("Member name: ", p.getString("title"));
                }
                ListView list = (ListView)findViewById(R.id.disp);
                list.setAdapter(new ArrayAdapter(Gmail_Auth.this,android.R.layout.simple_list_item_1,al));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}