package com.zzy.slidingmenu.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.zzy.luggage.*;

import android.app.PendingIntent;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;


public class HomeFragment extends Fragment implements OnClickListener{
	private Button mLocateButton;
	private Button mBleUnlockButton;
	private Button mSmsUnlockButton;
	private Button mDistanceButton;
	private Button mWeightButton;
	private Button mBatteryButton;
	private Button mRegisterFingerButton;
	private Button mDeleteFingerButton;

	private String mRemotePhoneNumber;
	
    private BluetoothLeService mBluetoothLeService;
    private BluetoothGattCharacteristic mNotifyCharacteristic;
    private BluetoothGattCharacteristic mWriteCharacteristic;
 
    private boolean mConnected = false;
    private boolean bonded = false;
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e("HOME", "Unable to initialize Bluetooth");
                //finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(LuggageActivity.luggageAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };
    
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.v("device control", "receive action"+action);
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                Toast.makeText(getActivity().getApplicationContext(), "设备已连接",
                Toast.LENGTH_LONG).show();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                Toast.makeText(getActivity().getApplicationContext(), "设备已断开",
                Toast.LENGTH_LONG).show();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
            	ConfigGattServices(mBluetoothLeService.getSupportedGattServices());
            } 
            else if (BluetoothLeService.ACTION_GATT_CONFIG_DESC_SUCCEED.equals(action)) {
            	sendData(DeviceControlActivity.GET_SIM_CMD);
            }
            else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                Toast.makeText(getActivity().getApplicationContext(), "收到数据"+intent.getStringExtra(BluetoothLeService.EXTRA_DATA),
                Toast.LENGTH_LONG).show();
            }
        }
    };
    
    private void ConfigGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                if ((gattCharacteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                    mNotifyCharacteristic = gattCharacteristic;
                    mBluetoothLeService.setCharacteristicNotification(
                    		gattCharacteristic, true);
                }
                if ((gattCharacteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
                	mWriteCharacteristic = gattCharacteristic;
                }
            }
        }
    }
    
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONFIG_DESC_SUCCEED);

        return intentFilter;
    }
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		if(LuggageActivity.luggageAddress != null)
			bonded = true;
		else bonded = false;
		
		if(bonded){
			Intent gattServiceIntent = new Intent(getActivity(), BluetoothLeService.class);
			getActivity().bindService(gattServiceIntent, mServiceConnection, getActivity().BIND_AUTO_CREATE);
		}
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_home, null);
		
		mLocateButton = (Button) view.findViewById(R.id.locate);
		mBleUnlockButton = (Button) view.findViewById(R.id.ble_unlock);
		mSmsUnlockButton = (Button) view.findViewById(R.id.sms_unlock);
		mDistanceButton = (Button) view.findViewById(R.id.distance);
		mWeightButton = (Button) view.findViewById(R.id.weight);
		mBatteryButton = (Button) view.findViewById(R.id.battery);
		mRegisterFingerButton = (Button) view.findViewById(R.id.register_finger);
		mDeleteFingerButton = (Button) view.findViewById(R.id.delete_finger);
		
		
		mLocateButton.setOnClickListener(this);
		mBleUnlockButton.setOnClickListener(this);
		mSmsUnlockButton.setOnClickListener(this);
		mDistanceButton.setOnClickListener(this);
		mWeightButton.setOnClickListener(this);
		mBatteryButton.setOnClickListener(this);
		mRegisterFingerButton.setOnClickListener(this);
		mDeleteFingerButton.setOnClickListener(this);

		return view;
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
	 @Override
	public void onResume() {
	        super.onResume();
			if(bonded){
		        getActivity().registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
		        if (mBluetoothLeService != null) {
		            final boolean result = mBluetoothLeService.connect(LuggageActivity.luggageAddress);
		            Log.d("HOME", "Connect request result=" + result);
		        }
			}
	    }

	  @Override
	public void onPause() {
	        super.onPause();
			if(bonded){
				getActivity().unregisterReceiver(mGattUpdateReceiver);
			}
	 }
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(bonded){
			getActivity().unbindService(mServiceConnection);
			mBluetoothLeService = null;
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.locate: 
			this.LocateOnClick();
			break;
		case R.id.ble_unlock:
			this.BleUnlockOnClick();
			break;
		case R.id.sms_unlock:
			this.SmsUnlockOnClick();
			break;
		case R.id.distance:
			this.DistanceOnClick();
			break;
		case R.id.weight:
			this.WeightOnClick();
			break;
		case R.id.battery:
			this.BatteryOnClick();
			break;
		case R.id.register_finger:
			this.RegisterFingerOnClick();
			break;
		case R.id.delete_finger:
			this.DeleteFingerOnClick();
			break;	
		default:
			Log.d("home","click");
			break;
		}
	}
	//Locate Button Click Event Process
	public void LocateOnClick()
	{
		Log.d("LuggageActivity", "LocateOnClick");
	//	Toast.makeText(getApplicationContext(), "locate button",
	//	Toast.LENGTH_SHORT).show();
		
		Intent intent = new Intent(getActivity(), LuggageLocateActivity.class);
		startActivity(intent);
	}
	
	//BLE Unlock Button Click Event Process
	public void BleUnlockOnClick()
	{
		//Toast.makeText(getApplicationContext(), "BLE Unlock button",
		//Toast.LENGTH_SHORT).show();
		 Toast.makeText(getActivity().getApplicationContext(), "设备未绑定", Toast.LENGTH_SHORT).show();
	}
	
	//Sms Unlock Button Click Event Process
	public void SmsUnlockOnClick()
	{
		//Toast.makeText(getApplicationContext(), "Sms Unlock button",
		//Toast.LENGTH_SHORT).show();
		// PendingIntent pi = PendingIntent.getActivity(getActivity(), 0, new Intent(getActivity(), testSms.class), 0);
		 SmsManager sms = SmsManager.getDefault();
		 if(mRemotePhoneNumber == null)
		 {
			 Toast.makeText(getActivity().getApplicationContext(), "设备未绑定", Toast.LENGTH_SHORT).show();
		 }
		 else 
		{
			 sms.sendTextMessage(mRemotePhoneNumber, null, "KS", null, null);
			 Toast.makeText(getActivity().getApplicationContext(), "远程开锁命令已发送", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	//distance Button Click Event Process
	public void DistanceOnClick()
	{
		Toast.makeText(getActivity().getApplicationContext(), "distance button",
		Toast.LENGTH_SHORT).show();
	}
	
	public void WeightOnClick()
	{
		Toast.makeText(getActivity().getApplicationContext(), "weight button",
		Toast.LENGTH_SHORT).show();

	}
	
	public void BatteryOnClick()
	{
		Toast.makeText(getActivity().getApplicationContext(), "batter button",
		Toast.LENGTH_SHORT).show();
	}
	
	public void DeleteFingerOnClick()
	{
		Toast.makeText(getActivity().getApplicationContext(), "delete finger button",
		Toast.LENGTH_SHORT).show();

	}
	
	public void RegisterFingerOnClick()
	{
		Toast.makeText(getActivity().getApplicationContext(), "register finger button",
		Toast.LENGTH_SHORT).show();

	}
	
	public void sendData(String data)
	    {
	    	if(mWriteCharacteristic != null)
	    	{
	    		mWriteCharacteristic.setValue(data);
	    		mBluetoothLeService.writeCharacteristic(mWriteCharacteristic);
	    	}
	    }
}
