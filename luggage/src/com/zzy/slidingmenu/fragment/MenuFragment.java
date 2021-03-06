package com.zzy.slidingmenu.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zzy.luggage.*;

public class MenuFragment extends Fragment implements OnClickListener{
	private View homeView;
	private View footView;
	private View newProductView;
	private View bookView;
	private View bleScanView;
	private View settingsView;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.slide_menu, null);
		findViews(view);
		
		return view;
	}
	
	
	public void findViews(View view) {
		homeView = view.findViewById(R.id.tvHome);
		bleScanView = view.findViewById(R.id.tvScanView);

		
		footView = view.findViewById(R.id.tvFoot);
		newProductView = view.findViewById(R.id.tvNewProduct);
		bookView = view.findViewById(R.id.tvBook);
		settingsView = view.findViewById(R.id.tvMySettings);
		
		homeView.setOnClickListener(this);
		bleScanView.setOnClickListener(this);
		
		footView.setOnClickListener(this);
		newProductView.setOnClickListener(this);
		bookView.setOnClickListener(this);
		settingsView.setOnClickListener(this);
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		Fragment newContent = null;
		String title = null;
		Intent intent;
		switch (v.getId()) {
		case R.id.tvHome: 
			newContent = new HomeFragment();
			title = getString(R.string.home);
			break;
		case R.id.tvScanView: 
			SharedPreferences mySharedPreferences= getActivity().getSharedPreferences("devices", 
	        		getActivity().MODE_PRIVATE); 
			String luggageAddress =mySharedPreferences.getString("Address", ""); 

			if(luggageAddress != "")
			{
				newContent = null;//new FootFragment();
				title = null;//getString(R.string.foot);		
				Toast.makeText(getActivity().getApplicationContext(), "已存在绑定设备",
					Toast.LENGTH_SHORT).show();
			
			}
			else
			{
				intent = new Intent(getActivity(), DeviceScanActivity.class);
				startActivity(intent);
			}
			break;
			
		case R.id.tvFoot:
			newContent = null;//new FootFragment();
			title = null;//getString(R.string.foot);
			
			intent = new Intent(getActivity(), LuggageLocateActivity.class);
			startActivity(intent);
			break;
		case R.id.tvNewProduct: 
			intent = new Intent(getActivity(), LuggageWebsiteActivity.class);
	        intent.putExtra("URL", "http://www.americantourister.com.cn/pub1/Products.aspx");
			startActivity(intent);
			newContent = null;//new NewProductFragment();
			title = null;//getString(R.string.new_product);
			break;
		case R.id.tvBook: 
			intent = new Intent(getActivity(), LuggageWebsiteActivity.class);
	        intent.putExtra("URL", "http://www.ctrip.com/");
			startActivity(intent);
			newContent = null;//new BookFragment();
			title = null;//getString(R.string.book);
			break;
		
		case R.id.tvMySettings: 
			newContent = new SettingsFragment();
			title = getString(R.string.settings);
			break;
			
		default:
			break;
		}
		if (newContent != null) {
			switchFragment(newContent, title);
		}
	}
	

	private void switchFragment(Fragment fragment, String title) {
		if (getActivity() == null) {
			return;
		}
		if (getActivity() instanceof LuggageActivity) {
			LuggageActivity mainActivity = (LuggageActivity) getActivity();
			mainActivity.switchConent(fragment, title);
		}
	}
	
}
