package com.example.firstproject.compactdrive;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Gmail_Auth extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmail__auth);
        Button btn_tok = (Button) findViewById(R.id.tok_generate);
        btn_tok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = "DEMO";
                Intent temp = new Intent(view.getContext(),Auth.class);
                startActivityForResult(temp,1);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                TextView tok = (TextView)findViewById(R.id.token_string);
                String code = data.getStringExtra(Auth.CODE);
                tok.setText(code);

            }
        }
    }
}
