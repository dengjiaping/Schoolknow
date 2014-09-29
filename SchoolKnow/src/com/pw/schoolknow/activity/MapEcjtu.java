package com.pw.schoolknow.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.mapapi.map.MyLocationOverlay.LocationMode;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pw.schoolknow.R;
import com.pw.schoolknow.app.MyApplication;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.utils.BMapUtil;
import com.pw.schoolknow.widgets.MyProgressBar;



public class MapEcjtu extends BaseActivity {
	
	public BMapManager mBMapMan = null;  
	public static MapView mMapView = null;
	private MapController mMapController = null;
	
	@ViewInject(R.id.map_location_btn)  
	private ImageButton locationBtn;
	
	// 定位相关
	LocationClient mLocClient;
	LocationData locData = null;
	public MyLocationListenner myListener = new MyLocationListenner();
		
	//定位图层
	locationOverlay myLocationOverlay = null;
	
	public static MyProgressBar mpb;
	
	//弹出泡泡图层
	private PopupOverlay   pop  = null;//弹出泡泡图层，浏览节点时使用
	private TextView  popupText = null;//泡泡view
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	 
		mBMapMan=new BMapManager(getApplication());  
		mBMapMan.init(MyApplication.BaiduMapApi, null); 
		
		setContentView(R.layout.act_map_location);
		setTitle("交大地图");
		setTitleBar(R.drawable.btn_titlebar_back,0);
		ViewUtils.inject(this);
		
		locationBtn.setOnClickListener(new BtnClick());
		
		
		mMapView=(MapView)findViewById(R.id.mapView);  
		mMapController = mMapView.getController();
		mMapView.setBuiltInZoomControls(true); 
		mMapView.getController().setZoom(14);
        mMapView.getController().enableClick(true);
		
		mpb=new MyProgressBar(this);
		mpb.setMessage("正在定位中...");
		
		//定位初始化
        mLocClient = new LocationClient( this );
        locData = new LocationData();
        mLocClient.registerLocationListener( myListener );
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);//打开gps
        option.setCoorType("bd09ll");     //设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
       
        //定位图层初始化
		myLocationOverlay = new locationOverlay(mMapView);
		//设置定位数据
	    myLocationOverlay.setData(locData);
	    //添加定位图层
		mMapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableCompass();
		//修改定位数据后刷新图层生效
		mMapView.refresh();
	}
	
	protected void onDestroy(){  
        mMapView.destroy();  
        if(mBMapMan!=null){  
                mBMapMan.destroy();  
                mBMapMan=null;  
        }  
        super.onDestroy();  
	}  
	protected void onPause(){  
	        mMapView.onPause();  
	        if(mBMapMan!=null){  
	               mBMapMan.stop();  
	        }  
	        super.onPause();  
	}   
	protected void onResume(){  
	        mMapView.onResume();  
	        if(mBMapMan!=null){  
	                mBMapMan.start();  
	        }  
	       super.onResume();  
	}  
	
	public class BtnClick implements OnClickListener{
		public void onClick(View view) {
			if(view==locationBtn){
				mLocClient.requestLocation();
				mpb.show();
			}
		}
		
	}
	
	public class MyLocationListenner implements BDLocationListener {
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
                return ;
            locData.latitude = location.getLatitude();
            locData.longitude = location.getLongitude();
            //如果不显示定位精度圈，将accuracy赋值为0即可
            locData.accuracy = location.getRadius();
            // 此处可以设置 locData的方向信息, 如果定位 SDK 未返回方向信息，用户可以自己实现罗盘功能添加方向信息。
            locData.direction = location.getDerect();
            //更新定位数据
            myLocationOverlay.setData(locData);
            //更新图层数据执行刷新后生效
            mMapView.refresh();
            mpb.dismiss();
            mMapController.animateTo(new GeoPoint((int)(locData.latitude* 1e6), (int)(locData.longitude *  1e6)));
            myLocationOverlay.setLocationMode(LocationMode.NORMAL);
		}

		@Override
		public void onReceivePoi(BDLocation arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	 //继承MyLocationOverlay重写dispatchTap实现点击处理
  	public class locationOverlay extends MyLocationOverlay{

  		public locationOverlay(MapView mapView) {
  			super(mapView);
  			// TODO Auto-generated constructor stub
  		}
  		@Override
  		protected boolean dispatchTap() {
  			// TODO Auto-generated method stub
  			//处理点击事件,弹出泡泡
  			popupText.setBackgroundResource(R.drawable.popup);
			popupText.setText("我的位置");
			pop.showPopup(BMapUtil.getBitmapFromView(popupText),
					new GeoPoint((int)(locData.latitude*1e6), (int)(locData.longitude*1e6)),
					8);
  			return true;
  		}
  		
  	}

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		switch(buttonId){
		case 1:
			finish();
			break;
		case 2:
			break;
		default:
			break;
		}
	}

}
