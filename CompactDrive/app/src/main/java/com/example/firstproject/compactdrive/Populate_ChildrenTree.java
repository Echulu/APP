package com.example.firstproject.compactdrive;

import android.app.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.util.Log;

import android.view.WindowManager;
import android.widget.ListView;

import org.json.JSONArray;

import org.json.JSONObject;


import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;



public class Populate_ChildrenTree extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gmail__auth);
        new Gmail().execute();
        Toolbar t = (Toolbar)findViewById(R.id.toolbar);
        t.setTitle("Compact Drive");
    }
    public class Gmail extends AsyncTask {

        private StringBuffer fileJson = new StringBuffer();

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
                            fileJson.append(inputLine);
                        }
                    } else if (c == 401) {
                        Client.refreshToken();
                        HttpURLConnection con2 = (HttpURLConnection) temp.openConnection();
                        con2.setRequestMethod("GET");
                        authToken = "OAuth " + Client.aceToken;
                        con2.setRequestProperty("Authorization", authToken);
                        c = con2.getResponseCode();
                        if (con2.getResponseCode() == 200) {
                            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                            String inputLine;
                            while ((inputLine = in.readLine()) != null) {
                                fileJson.append(inputLine);
                            }
                        }
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
            try {
                JSONObject fileList = new JSONObject(fileJson.toString());
                GoogleChildrenTree.populateTree(fileList);
                Intent children = new Intent(Populate_ChildrenTree.this,Populate_Children.class);
                children.putExtra(Library.PARENT, "root");
                startActivity(children);
            }catch(Exception e){
                e.printStackTrace();
            }
//            try {
//                JSONArray fileList =(JSONArray)new JSONObject(fileJson.toString()).get("items");
//                ArrayList<GfileObject> resultList = new ArrayList<>();
//                int fileCount = 0;
//                while(fileCount < fileList.length()){
//                    JSONObject temp = (JSONObject)fileList.get(fileCount);
//                    JSONObject label = (JSONObject)temp.get("labels");
//                    JSONArray parents = (JSONArray)temp.get("parents");
//                    boolean isRoot = false;
//                    for (int i =0; i < parents.length();i++) {
//                        if(((JSONObject)parents.get(0)).getBoolean("isRoot"))
//                                isRoot = true;
//                    }
//                    if(label.getString("trashed").equals("false")&&isRoot) {
//                        GfileObject tem = new GfileObject();
//                        tem.setID(temp.getString("id"));
//                        tem.setTitle(temp.getString("title"));
//                        tem.setMimeType(temp.getString("mimeType"));
//                        //tem.setOwner(temp.getString("owner"));
//                        tem.setDoc(temp.getString("createdDate"));
//                        resultList.add(tem);
//                    }
//
//                    fileCount++;
//                }
//                FileAdapter ap = new FileAdapter(Populate_ChildrenTree.this,resultList);
//                ListView list = (ListView)findViewById(R.id.disp);
//                list.setAdapter(ap);
//            }
//            catch (Exception e){
//                Log.i("Exception GMAIL_AUTH", e.getMessage());
//            }
        }
    }
}