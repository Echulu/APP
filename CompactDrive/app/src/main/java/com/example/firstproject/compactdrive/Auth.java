package com.example.firstproject.compactdrive;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Auth extends Activity {
    private String tok = "DEMO";
    public static String CODE ="DEMO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        WebView w = (WebView)findViewById(R.id.auth_view);
        w.getSettings().setJavaScriptEnabled(true);
        w.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        w.loadUrl(Google_Client.generatePermissionUrl());

        w.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                tok = view.getTitle();
                if (tok.split("=")[0].equals("Success code")) {
                    Intent temp = new Intent(view.getContext(), Auth.class);
                    Google_Client  g= new Google_Client();
                    g.populateTokens(tok.split("=")[1]);
                    temp.putExtra(CODE,g.aceToken);
                    setResult(RESULT_OK, temp);

                    finish();

                }
            }
        });

    }

}
