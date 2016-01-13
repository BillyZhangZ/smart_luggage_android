package com.zzy.luggage;  

import com.zzy.luggage.R;
import android.app.Activity;  
import android.app.backup.SharedPreferencesBackupHelper;  
import android.content.Context;  
import android.content.Intent;  
import android.content.SharedPreferences;  
import android.content.SharedPreferences.Editor;  
import android.os.Bundle;  
import android.text.Spannable;  
import android.view.View;  
import android.view.View.OnClickListener;  
import android.view.Window;  
import android.widget.Button;  
import android.widget.CheckBox;  
import android.widget.CompoundButton;  
import android.widget.CompoundButton.OnCheckedChangeListener;  
import android.widget.EditText;  
import android.widget.ImageButton;  
import android.widget.Toast;  
  
public class RegisterActivity extends Activity {  
      
    private EditText userName, password;  
    private Button btn_confirm, btn_verify;  
    private String userNameValue,passwordValue;  
    private SharedPreferences sp;  
  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
          
        //去除标题  
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.activity_login);  
          
        //获得实例对象  
        sp = this.getSharedPreferences("account", MODE_PRIVATE);  
        userName = (EditText) findViewById(R.id.et_zh);  
        password = (EditText) findViewById(R.id.et_mima);  
        btn_confirm = (Button) findViewById(R.id.btn_confirm);  
        btn_verify   = (Button) findViewById(R.id.btn_verify);  
          
        // 登录监听事件  现在默认为用户名为：liu 密码：123  
        btn_confirm.setOnClickListener(new OnClickListener() {  
  
            public void onClick(View v) {  
                
                  
            }  
        });  
  
        btn_verify.setOnClickListener(new OnClickListener() {  
        	  
            public void onClick(View v) {  
                
                   
            }  
        });  
  
    }  
}