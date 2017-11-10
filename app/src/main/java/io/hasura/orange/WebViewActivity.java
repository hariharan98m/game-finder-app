package io.hasura.orange;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends BaseActivity {
    String book_name;
    boolean flag=true;

    public static void startActivity(Activity startingActivity) {
        Intent intent = new Intent(startingActivity, WebViewActivity.class);
        startingActivity.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        setTitle("Developer | Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        WebView webView= (WebView) findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (flag) showDotProgressDialog("Just a bit",true);
                else{
                    showProgressDialog(true);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(flag) {showDotProgressDialog("",false);flag=false;}
                else
                    showProgressDialog(false);

            }
        });

        webView.loadUrl("http://hariharanm2016.imad.hasura-app.io/");
    }
}
