package com.zzy.luggage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Random;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_up);
		((LauncherTextView)findViewById(R.id.start)).setStart(true);
		
		mLogin = isLogin();

				
	//	new Handler().postDelayed(new Runnable() {    
	  //      public void run() {   
	            //你需要跳转的地方的代码  
	            final Intent intent = new Intent();
	            if(mLogin == true)
	            {
		            intent.setClass(StartUpActivity.this, LuggageActivity.class);
		            startActivity(intent);
		            finish();
	            }
	            else 
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
	    			            intent.setClass(StartUpActivity.this, LuggageActivity.class);
	    		        		startActivity(intent);
	    			            finish();
	    					}
	    				}
	    			});
	    			registerPage.show(this);
	        		//intent.setClass(StartUpActivity.this, LoginActivity.class);
	            }

	           // intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, device.getName());
	           /// intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, device.getAddress());
	           // startActivity(intent);
	           // finish();  
	      //  }    
	    //}, 3000); //延迟2秒跳转 
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
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start_up, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public boolean isLogin()
	{
		//实例化SharedPreferences对象（第一步） 
		SharedPreferences mySharedPreferences= getSharedPreferences("account", 
		MODE_PRIVATE); 
		//实例化SharedPreferences.Editor对象（第二步） 
		//SharedPreferences.Editor editor = mySharedPreferences.edit(); 
		//用putString的方法保存数据 
	//	editor.putString("name", "Karl"); 
	//	editor.putString("habit", "sleep"); 
		//提交当前数据 
//		editor.commit(); 		
		String phoneNumber =mySharedPreferences.getString("phoneNumber", ""); 
		//String habit =mySharedPreferences.getString("habit", ""); 
		//使用toast信息提示框显示信息 
		Toast.makeText(this , "账户："+"\n"+"phoneNumber：" + phoneNumber + "\n", 
		Toast.LENGTH_LONG).show(); 
		
		if(phoneNumber != "")
		return true;
		else return false;
	}
	
	public boolean Login(String number)
	{
		//实例化SharedPreferences对象（第一步） 
		SharedPreferences mySharedPreferences= getSharedPreferences("account", 
		MODE_PRIVATE); 
		//实例化SharedPreferences.Editor对象（第二步） 
		SharedPreferences.Editor editor = mySharedPreferences.edit(); 
		//用putString的方法保存数据 
		editor.putString("phoneNumber", number); 
		//提交当前数据 
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
		return false;
	}
}
