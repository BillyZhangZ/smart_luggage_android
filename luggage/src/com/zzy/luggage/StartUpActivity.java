package com.zzy.luggage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Random;

import com.zzy.pattern.GuideGesturePasswordActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.CommonDialog;
import cn.smssdk.gui.ContactsPage;
import cn.smssdk.gui.RegisterPage;
public class StartUpActivity extends Activity implements Callback{
	private boolean mLogin = false;
	
	private static String APPKEY = "ea9a66a8a5b0";
	private static String APPSECRET = "f81b88852c10afbc7b63ef415646d2b2";
	private boolean ready;

	public static final int EVENT_NEED_TO_LOGIN = 0x80;

	//control double click back key to exit app
    private long mExitTime;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_up);
		((LauncherTextView)findViewById(R.id.start)).setStart(true);
		
		mLogin = isLogin();
		new Handler().postDelayed(new Runnable() {    
	        public void run() {   
	            final Intent intent = new Intent();
	            if(mLogin == true)
	            {
		            intent.setClass(StartUpActivity.this, GuideGesturePasswordActivity.class);
	        		startActivity(intent);
		            //intent.setClass(StartUpActivity.this, LuggageActivity.class);
		            //startActivity(intent);
		            finish();
	            }
	            else 
	            {
	        		final Handler handler = new Handler(StartUpActivity.this);
	            	Message msg = new Message();
					msg.arg1 = EVENT_NEED_TO_LOGIN;
					handler.sendMessage(msg);
	            }
	        }    
	    }, 3000);  
	}
	private void initSDK() {
		SMSSDK.initSDK(this, APPKEY, APPSECRET, true);
		final Handler handler = new Handler(this);
		EventHandler eventHandler = new EventHandler() {
			public void afterEvent(int event, int result, Object data) {
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				handler.sendMessage(msg);
			}
		};

		SMSSDK.registerEventHandler(eventHandler);
		ready = true;
	}
	
	protected void onDestroy() {
		if (ready) {
			SMSSDK.unregisterAllEventHandler();
		}
		super.onDestroy();
	}
	
	public boolean isLogin()
	{
		//实锟斤拷锟斤拷SharedPreferences锟斤拷锟襟（碉拷一锟斤拷锟斤拷 
		SharedPreferences mySharedPreferences= getSharedPreferences("account", 
		MODE_PRIVATE); 
				
		String phoneNumber =mySharedPreferences.getString("phoneNumber", ""); 
		//Toast.makeText(this , "锟剿伙拷锟斤拷"+"\n"+"phoneNumber锟斤拷" + phoneNumber + "\n", 
		//Toast.LENGTH_LONG).show(); 
		
		if(phoneNumber != "")
			return true;
		else return false;
	}
	
	public boolean Login(String number)
	{
		//实锟斤拷锟斤拷SharedPreferences锟斤拷锟襟（碉拷一锟斤拷锟斤拷 
		SharedPreferences mySharedPreferences= getSharedPreferences("account", 
		MODE_PRIVATE); 
		//实锟斤拷锟斤拷SharedPreferences.Editor锟斤拷锟襟（第讹拷锟斤拷锟斤拷 
		SharedPreferences.Editor editor = mySharedPreferences.edit(); 
		//锟斤拷putString锟侥凤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷 
		editor.putString("phoneNumber", number); 
		//锟结交锟斤拷前锟斤拷锟斤拷 
		editor.commit(); 		
		
		return true;
	}
	
	public boolean handleMessage(Message msg) {
		int event = msg.arg1;
		int result = msg.arg2;
		Object data = msg.obj;
		if (event == SMSSDK.EVENT_SUBMIT_USER_INFO) {
			if (result == SMSSDK.RESULT_COMPLETE) {
				Toast.makeText(this, R.string.smssdk_user_info_submited, Toast.LENGTH_SHORT).show();
			} else {
				((Throwable) data).printStackTrace();
			}
		} 
		else if(event == EVENT_NEED_TO_LOGIN)
		{
			initSDK();
    		RegisterPage registerPage = new RegisterPage();
			registerPage.setRegisterCallback(new EventHandler() {
				public void afterEvent(int event, int result, Object data) {
					if (result == SMSSDK.RESULT_COMPLETE) {
						@SuppressWarnings("unchecked")
						HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
						String country = (String) phoneMap.get("country");
						String phone = (String) phoneMap.get("phone");
						Login(phone);
						//registerUser(country, phone);
						Intent intent = new Intent();
			            intent.setClass(StartUpActivity.this, LuggageActivity.class);
		        		startActivity(intent);
			            finish();
					}
				}
			});
			registerPage.show(this);
		}
		return false;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if(keyCode == KeyEvent.KEYCODE_BACK) { //锟斤拷锟�/锟斤拷锟斤拷/锟斤拷锟轿凤拷锟截硷拷
			 if ((System.currentTimeMillis() - mExitTime) > 2000) {
                // Object mHelperUtils;
                 Toast.makeText(this, "锟劫帮拷一锟斤拷锟剿筹拷锟斤拷锟斤拷", Toast.LENGTH_SHORT).show();
                 mExitTime = System.currentTimeMillis();

	         } else {
	                 finish();
	         }
	         return true;
         } else if(keyCode == KeyEvent.KEYCODE_MENU) {//MENU锟斤拷
	        //锟斤拷锟�/锟斤拷锟截菜碉拷锟斤拷
	         return true;
	    }     
	    return super.onKeyDown(keyCode, event);
	}
}
