package com.example.petlet;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
public class WebviewDemo extends AppCompatActivity {
    private WebView webview1;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_demo);

//        if (getSupportActionBar() != null) {
//            getSupportActionBar().hide();
//        }

        webview1 = (WebView) findViewById(R.id.webview1);
        webview1.setWebViewClient(new WebViewClient());
        webview1.loadUrl("https://www.petfinder.com/");
        WebSettings webSettings = webview1.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }
    @Override
    public void onBackPressed() {
        if(webview1.canGoBack()){
            webview1.goBack();
        }
        else{
            super.onBackPressed();
        }
    }
}