package com.example.firstproject.compactdrive;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by cgadi on 11/25/2015.
 */
public class Populate_Children extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String parent = "root";
            try {
                JSONArray fileList =GoogleChildrenTree.getChildrenByParent(parent);
                ArrayList<GfileObject> resultList = new ArrayList<>();
                int fileCount = 0;
                while(fileCount < fileList.length()){
                    JSONObject temp = (JSONObject)fileList.get(fileCount);
                    JSONObject label = (JSONObject)temp.get("labels");
                    JSONArray parents = (JSONArray)temp.get("parents");

                    if(label.getString("trashed").equals("false")) {
                        GfileObject tem = new GfileObject();
                        tem.setID(temp.getString("id"));
                        tem.setTitle(temp.getString("title"));
                        tem.setMimeType(temp.getString("mimeType"));
                        //tem.setOwner(temp.getString("owner"));
                        tem.setDoc(temp.getString("createdDate"));
                        resultList.add(tem);
                    }
                    fileCount++;
                }
                FileAdapter ap = new FileAdapter(Populate_Children.this,resultList);
                ListView list = (ListView)findViewById(R.id.disp);
                list.setAdapter(ap);
            }
            catch (Exception e){
                Log.i("Exception at children", e.getMessage());
            }


    }
}
