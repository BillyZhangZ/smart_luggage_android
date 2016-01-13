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
  
public class LoginActivity extends Activity {  
      
    private EditText userName, password;  
    private Button btn_login, btn_register, btn_test;  
    private String userNameValue,passwordValue;  
    private SharedPreferences sp;  
  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
          
        //ȥ������  
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.activity_login);  
          
        //���ʵ������  
        sp = this.getSharedPreferences("account", MODE_PRIVATE);  
        userName = (EditText) findViewById(R.id.et_zh);  
        password = (EditText) findViewById(R.id.et_mima);  
        btn_login = (Button) findViewById(R.id.btn_login);  
        btn_register   = (Button) findViewById(R.id.btn_register);  
        btn_test   = (Button) findViewById(R.id.test);  
          
        // ��¼�����¼�  ����Ĭ��Ϊ�û���Ϊ��liu ���룺123  
        btn_login.setOnClickListener(new OnClickListener() {  
  
            public void onClick(View v) {  
                userNameValue = userName.getText().toString();  
                passwordValue = password.getText().toString();  
                  
                if(userNameValue.equals("zzy")&&passwordValue.equals("123456"))  
                {  
                    Toast.makeText(LoginActivity.this,"��¼�ɹ�", Toast.LENGTH_SHORT).show();  
                    //��¼�ɹ��ͼ�ס�����Ϊѡ��״̬�ű����û���Ϣ  
                    if(true)  
                    {  
                     //��ס�û��������롢  
                      Editor editor = sp.edit();  
                      editor.putString("USER_NAME", userNameValue);  
                      editor.putString("PASSWORD",passwordValue);  
                      editor.commit();  
                    }  
                    //��ת����  
                    Intent intent = new Intent(LoginActivity.this, LuggageActivity.class);  
                    LoginActivity.this.startActivity(intent);  
                    //finish();  
                      
                }else{  
                      
                    Toast.makeText(LoginActivity.this,"�û�����������������µ�¼", Toast.LENGTH_LONG).show();  
                }  
                  
            }  
        });  
  
        btn_register.setOnClickListener(new OnClickListener() {  
        	  
            public void onClick(View v) {  
                
                    //��ת����  
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);  
                    LoginActivity.this.startActivity(intent);  
            }  
        });  
        
        btn_test.setOnClickListener(new OnClickListener() {  
      	  
            public void onClick(View v) {  
                
                    //��ת����  
                    Intent intent = new Intent(LoginActivity.this, LuggageActivity.class);  
                    LoginActivity.this.startActivity(intent);  
            }  
        });  
  
    }  
}