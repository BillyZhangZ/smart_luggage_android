package com.zzy.slidingmenu.fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.zzy.luggage.*;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FootFragment extends Fragment {
	private MapView mapView;
	private AMap aMap;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_foot, null);
		mapView = (MapView) view.findViewById(R.id.footMapView);
		mapView.onCreate(savedInstanceState);// �˷���������д
		init(); 
		
		return view;
	}
	
	
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}
	
	/**
	 * ��ʼ��AMap����
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
		
	}
	
	/**
	 * ����һЩamap������
	 */
	private void setUpMap() {
		// �Զ���ϵͳ��λС����
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.location_marker));// ����С�����ͼ��
		myLocationStyle.strokeColor(Color.BLACK);// ����Բ�εı߿���ɫ
		myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// ����Բ�ε������ɫ
		// myLocationStyle.anchor(int,int)//����С�����ê��
		myLocationStyle.strokeWidth(1.0f);// ����Բ�εı߿��ϸ
		aMap.setMyLocationStyle(myLocationStyle);
		//aMap.setLocationSource((LocationSource) this);// ���ö�λ����
		aMap.getUiSettings().setMyLocationButtonEnabled(false);// ����Ĭ�϶�λ��ť�Ƿ���ʾ
		aMap.setMyLocationEnabled(false);// ����Ϊtrue��ʾ��ʾ��λ�㲢�ɴ�����λ��false��ʾ���ض�λ�㲢���ɴ�����λ��Ĭ����false
	   // aMap.setMyLocationType()
		// TODO Auto-generated method stub
		new AsyncTask<String, Void, Void>(){
			@Override
			protected Void doInBackground(String... params) {
				// TODO Auto-generated method stub
				try {
					URL url = new URL(params[0]);
					URLConnection connection = url.openConnection();
					InputStream is = connection.getInputStream();
					InputStreamReader isr = new InputStreamReader(is, "utf-8");
					BufferedReader br = new BufferedReader(isr);
					
					String line;
					while((line = br.readLine())!= null)
					{
						System.out.println(line);
						//addMarkersToMap();
					}
					br.close();
					isr.close();
					is.close();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return null;
			}
			
		}.execute("http://121.40.128.16/api/gps/1");
	}
	
}
