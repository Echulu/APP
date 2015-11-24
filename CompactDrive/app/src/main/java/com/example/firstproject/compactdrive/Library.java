package com.example.firstproject.compactdrive;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import org.json.JSONObject;

public class Library extends Activity {

    private static  String D = "DEMO";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        Toolbar t = (Toolbar)findViewById(R.id.toolbar);
        t.setTitle("Compact Drive");
        Button gmail = (Button)findViewById(R.id.gmail);
        gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent temp = new Intent(v.getContext(), Auth.class);
                startActivityForResult(temp,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                Intent gmail = new Intent(Library.this,Gmail_Auth.class);
                startActivity(gmail);
            }
        }
    }
}
