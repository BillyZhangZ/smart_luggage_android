package com.zzy.luggage;

import android.app.Activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.net.http.SslError;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class LuggageWebsiteActivity extends Activity
{
    private WebView mWebView = null;// 用于显示结果，用载入html字符串的方式显示响应结果，而不是使用WebView自己的方式加载URL
    private String mUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website);
        
        final Intent intent = getIntent();
        mUrl = intent.getStringExtra("URL");
        
        init();
    }

    private void init(){
    	mWebView = (WebView) findViewById(R.id.webview);
    //	mWebView.getSettings().setDomStorageEnabled(true);
    	//mWebView.getSettings().setSupportZoom(true);  

        //WebView加载web资源
    	mWebView.loadUrl(mUrl);
    	mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
    	mWebView.setWebViewClient(new WebViewClient(){
    		
           @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
             view.loadUrl(url);
            return true;
        }
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                   SslError error) {
           // handler.proceed();  
           }
          
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
        	//view.loadUrl(url);
          // 	super.onPageStarted(view, url, favicon);
           }
           
       });
    }
}