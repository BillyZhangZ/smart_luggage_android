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
    private WebView mWebView = null;// ������ʾ�����������html�ַ����ķ�ʽ��ʾ��Ӧ�����������ʹ��WebView�Լ��ķ�ʽ����URL
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

        //WebView����web��Դ
    	mWebView.loadUrl(mUrl);
    	mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //����WebViewĬ��ʹ�õ�������ϵͳĬ�����������ҳ����Ϊ��ʹ��ҳ��WebView��
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