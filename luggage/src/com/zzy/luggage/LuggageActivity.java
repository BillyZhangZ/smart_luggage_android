package com.zzy.luggage;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.zzy.slidingmenu.fragment.*;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LuggageActivity extends SlidingFragmentActivity implements
OnClickListener{
	private ImageView topButton;
	private Fragment mContent;
	private TextView topTextView;
	
	public static String luggageAddress = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_luggage);
		initSlidingMenu(savedInstanceState);
		//switchConent(mContent, "Luggage");
		
		final Intent intent = getIntent();
		//from DeviceControlActivity
		if(intent.getAction() != null && intent.getAction().equals(DeviceControlActivity.DEVICE_BONDED))
		{
			luggageAddress = intent.getStringExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS);
		}
		
		topButton = (ImageView) findViewById(R.id.topButton);
		topButton.setOnClickListener(this);
		topTextView = (TextView) findViewById(R.id.topTv);
	}
	private void initSlidingMenu(Bundle savedInstanceState) {
		//
		if (savedInstanceState != null) {
			mContent = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContent");
		}

		if (mContent == null) {
			mContent = new HomeFragment();
		}

		//
		setBehindContentView(R.layout.misc_menu_frame_left);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new MenuFragment()).commit();
		
		getSupportFragmentManager().beginTransaction()
		.replace(R.id.content_frame, mContent).commit();
		getSlidingMenu().showContent();

		// 设置侧边栏属性值
		SlidingMenu sm = getSlidingMenu();
		// 
		sm.setMode(SlidingMenu.LEFT);
		// 
		sm.setShadowWidthRes(R.dimen.shadow_width);
		//
		sm.setShadowDrawable(null);
		// 
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 
		sm.setFadeDegree(0.35f);
		//
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		// 
		sm.setBehindScrollScale(0.0f);
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}

	
	public void switchConent(Fragment fragment, String title) {
		mContent = fragment;
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
		getSlidingMenu().showContent();
		topTextView.setText(title);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.topButton:
			toggle();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.luggage, menu);
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
}
