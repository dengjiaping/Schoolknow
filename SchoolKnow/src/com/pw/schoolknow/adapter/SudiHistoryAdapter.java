package com.pw.schoolknow.adapter;

import java.util.List;

import com.pw.schoolknow.R;
import com.pw.schoolknow.bean.SudiBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SudiHistoryAdapter extends BaseAdapter{
	
	private List<SudiBean> list;
	public Context context;
	
	public SudiHistoryAdapter(List<SudiBean> list, Context context) {
		super();
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
	        convertView = LayoutInflater.from(context)
	          .inflate(R.layout.sudi_list_lv_item, parent, false);
	    }
		TextView tv=(TextView) convertView;
		SudiBean item=list.get(position);
		tv.setText(item.getSudiName()+"("+item.getBookid()+")");
		return convertView;
	}

}
