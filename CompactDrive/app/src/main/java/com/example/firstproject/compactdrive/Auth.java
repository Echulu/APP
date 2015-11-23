package com.example.firstproject.compactdrive;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Auth extends Activity {
    private String tok = "DEMO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        if(Client.CODE == null) {
            WebView w = (WebView) findViewById(R.id.auth_view);
            w.getSettings().setJavaScriptEnabled(true);
            w.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            w.loadUrl(Client.generatePermissionUrl());
            w.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    String cod = view.getTitle();
                    if (cod.split("=")[0].equals("Success code")) {
                        Intent temp = new Intent(view.getContext(), Auth.class);
                        Client.CODE = cod.split("=")[1];
                        setResult(RESULT_OK, temp);
                        finish();
                    }
                }
            });
        }
        else{
            finish();
        }

    }

}
