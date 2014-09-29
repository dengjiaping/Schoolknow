package com.pw.schoolknow.activity;



import java.util.ArrayList;
import java.util.List;

import com.pw.schoolknow.R;
import com.pw.schoolknow.adapter.GalleryAdapter;
import com.pw.schoolknow.helper.SystemHelper;
import com.pw.schoolknow.widgets.QGallery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class PhotoView extends Activity {
	
		//屏幕的宽度
		public static int screenWidth;
		//屏幕的高度
		public static int screenHeight;
		private QGallery gallery;
		private TextView tv;
		
		public List<String> list;
		public int position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(com.pw.schoolknow.R.layout.act_photo_view);
		
		tv=(TextView) super.findViewById(R.id.mygallery_num);
		
		screenWidth=SystemHelper.getScreenWidth(this);
		screenHeight=SystemHelper.getScreenHeight(this);
		
		
		list=new ArrayList<String>();
		Intent it=this.getIntent();
        list=it.getStringArrayListExtra("data");
        position=it.getIntExtra("position", 0);
        
        gallery = (QGallery) findViewById(R.id.mygallery);
        gallery.setVerticalFadingEdgeEnabled(false);	
        gallery.setHorizontalFadingEdgeEnabled(false);//);// 设置view在水平滚动时，水平边不淡出。
        gallery.setAdapter(new GalleryAdapter(this,tv,list));
        gallery.setSelection(position);
	}
	
}
