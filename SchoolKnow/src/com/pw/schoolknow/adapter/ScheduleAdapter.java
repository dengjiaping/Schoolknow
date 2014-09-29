package com.pw.schoolknow.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pw.schoolknow.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;




public class ScheduleAdapter extends BaseAdapter{
	
	private Context context=null;
	private LayoutInflater mInflater=null;
	private int[] mTo;
	private String[] mFrom;
	private int mResource;
	private List<? extends Map<String, ?>> mData;
	
	
	public ScheduleAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		this.context=context;
		this.mTo=to;
		this.mResource=resource;
		this.mData=data;
		this.mFrom=from;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		  return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		 return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v=null;
		if(convertView==null){
			v=mInflater.inflate(mResource,parent,false);
		}else{
			v=convertView;
		}
		TextView text[]=new TextView[mTo.length];
		
		//初始化组件
		HashMap<String,?> hashmap = null;
	    hashmap=(HashMap<String, ?>) mData.get(position);
		for(int j=0;j<mTo.length;j++){			
	    	 text[j]=(TextView) v.findViewById(mTo[j]);
	    	 if(hashmap.get(mFrom[j]).toString().equals("")){
	    		 text[j].setVisibility(View.GONE); 
	    	 }else{
	    		 text[j].setText(hashmap.get(mFrom[j]).toString());
	    		 Drawable d=null;
	    		 Resources res = context.getResources();
	    		 switch(j){
	    		 case 0:
	    		 case 3:
	    			 d = res.getDrawable(R.drawable.pic_subject);
	    			 d.setBounds(0, 0, d.getIntrinsicWidth(),d.getIntrinsicHeight());
	    			 break;
	    		 case 1:
	    		 case 4:
	    			 d = res.getDrawable(R.drawable.pic_address);
	    			 d.setBounds(0, 0,d.getIntrinsicWidth(),d.getIntrinsicHeight());
	    			 break;
	    		 case 2:
	    		 case 5:
	    			 d = res.getDrawable(R.drawable.pic_time);
	    			 d.setBounds(0, 0, d.getIntrinsicWidth(),d.getIntrinsicHeight());
	    			 break;
	    		 default:
	    			 break;
	    		 }
	    		 text[j].setCompoundDrawables(d, null, null, null); //设置左图标
	    	 }
	    }
		return v;
	}


	

}
