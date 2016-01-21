package com.zzy.luggage;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnCameraChangeListener;
import com.amap.api.maps2d.LocationSource.OnLocationChangedListener;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.Text;
import com.amap.api.maps2d.model.TextOptions;
import com.amap.api.maps2d.model.TileOverlay;
import com.amap.api.maps2d.model.UrlTileProvider;
import com.amap.api.maps2d.overlay.*;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.amap.api.maps2d.CameraUpdateFactory;

public class LuggageLocateActivity extends  Activity {
	private MapView mapView;
	private AMap aMap;
	private MarkerOptions markerOption;
	private double mLatitude;
	private double mLongtitude;
	private double mAltitude;
	
	final int GPS_UPDATED = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_locate);
		
		mapView = (MapView) findViewById(R.id.mapView);
		mapView.onCreate(savedInstanceState);// �˷���������д
		init(); 
	}
	/*�����̨���ݸ�����Ϣ*/
	Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg){
				String s = String.valueOf(msg.what);
				addMarkersToMap();
			}
		};
		
		
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
				.fromResource(R.drawable.locate_location_marker));// ����С�����ͼ��
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
						allInfoFromJson(line);
						//this is background
						//handler.postDelayed(r, delayMillis)
					}
					br.close();
					isr.close();
					is.close();
					handler.sendEmptyMessage(GPS_UPDATED);

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
	
	public void allInfoFromJson(String jsonStr){

        /*{
"userId":1,
"timeStamp":"2015-12-06 08:14:48",    //UTCʱ�䣬��ʽΪ yyyy-mm-dd hh:mm:ss
"latitude":31.123456489,        //γ�ȣ�����9λС��
    //������Ϊ�������ϰ���Ϊ����           
"longtitude":121.123456789,    //���ȣ�����9λС��
        //������Ϊ������������Ϊ����
"altitude":-16.8,            //GPGGA���Σ���λΪM
"vAccuracy":0.90,         //GPGSA��ֱ�������ӣ�0.5~99.9
"hAccuracy":1.51,         //GPGSAˮƽ�������ӣ�0.5~99.9
"speed":0.000             //GPRMC�ٶȣ���λΪkm/h
} */
        try {
            //JSONObject jsonObject=new JSONObject(jsonStr).getJSONObject("list");

            @SuppressWarnings("deprecation")
            JSONObject gps=new JSONObject(jsonStr);
			//JSONArray jsonArray=new JSONObject(jsonStr).getJSONArray("list");

          //  for(int i=0;i<jsonArray.length();i++){
               // JSONObject jsonObject=(JSONObject)jsonArray.get(i);

/*
                String busLine=jsonObject.getString("busLine");
                String busName=jsonObject.getString("busName");
                Integer cityId=jsonObject.getInt("cityId"); 
                Integer districtId=jsonObject.getInt("districtId"); 
                String firstTime=jsonObject.getString("firstTime");
                String lastTime=jsonObject.getString("lastTime");
                */
                mLatitude=gps.getDouble("latitude");
                mLongtitude=gps.getDouble("longtitude");
                mAltitude =gps.getDouble("altitude");
            //}
            
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

	     
	/**
	 * ����������д
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * ����������д
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	/**
	 * ����������д
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	
	/**
	 * ����������д
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}
	
	/**
	 * �ڵ�ͼ�����marker
	 */
	private void addMarkersToMap() {

		LatLng marker1 = new LatLng(mLatitude, mLongtitude);	//�������ĵ�����ű��� 
		LatLonPoint loc = new LatLonPoint(mLatitude,mLongtitude);
		//getPositionName(loc);
		aMap.moveCamera(CameraUpdateFactory.changeLatLng(marker1)); 
		aMap.moveCamera(CameraUpdateFactory.zoomTo(12)); 
		
		
		//������ʾ��ע������������ʾ���ݣ�λ�ã������С��ɫ������ɫ��ת�Ƕ�,Zֵ��
		TextOptions textOptions = new TextOptions().position(marker1)//beijing
				.text("Text").fontColor(Color.BLACK)
				.backgroundColor(Color.BLUE).fontSize(30).rotate(20).align(Text.ALIGN_CENTER_HORIZONTAL, Text.ALIGN_CENTER_VERTICAL)
				.zIndex(1.f).typeface(Typeface.DEFAULT_BOLD)
				;
		//aMap.addText(textOptions);
		aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
				.position(new LatLng(mLatitude,mLongtitude)).title("")
				.snippet(Double.toString(mLatitude)+","+Double.toString(mLongtitude)).draggable(true));
		
	}
	
	public void getPositionName(LatLonPoint latLonPoint)
	{
		GeocodeSearch geocoderSearch = new GeocodeSearch(this); 
		geocoderSearch.setOnGeocodeSearchListener((OnGeocodeSearchListener) this); 
		//latLonPoint������ʾһ��Latlng���ڶ�������ʾ��Χ�����ף�GeocodeSearch.AMAP��ʾ�ǹ��������ϵ����GPSԭ������ϵ   
		RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP); 
		geocoderSearch.getFromLocationAsyn(query); 
	}

	//��������ص��ӿ�
	public void onRegeocodeSearched(RegeocodeResult  result, int rCode) { 
			//����result��ȡ����������
		Log.v("LuggageLocateActivity", result.getRegeocodeAddress().getBuilding());
		aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
				.position(new LatLng(mLatitude,mLongtitude)).title(result.getRegeocodeAddress().getBuilding())
				.snippet(Double.toString(mLatitude)+","+Double.toString(mLongtitude)).draggable(true));
	}
	
	
	
}
