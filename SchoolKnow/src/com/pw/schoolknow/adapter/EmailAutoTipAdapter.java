package com.pw.schoolknow.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class EmailAutoTipAdapter extends BaseAdapter implements Filterable {
	
	public static String[] emailArray = {"@qq.com","@hotmail.com","@126.com","@163.com","@gmail.com","@foxmail.com",
		"@sina.com", "@taobao.com"};  
	
	 public List<String> mList;  
     private Context mContext;  
     private MyFilter mFilter;  
     
     public EmailAutoTipAdapter(Context context) {  
         mContext = context;  
         mList = new ArrayList<String>();  
     }  

	@Override
	public Filter getFilter() {
		 if (mFilter == null) {  
             mFilter = new MyFilter();  
         }  
         return mFilter;
	}


	@Override
	public int getCount() {
		 return mList == null ? 0 : mList.size();  
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		 return mList == null ? null : mList.get(position);  
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		 if (convertView == null) {  
             TextView tv = new TextView(mContext);  
             tv.setTextColor(Color.BLACK);  
             tv.setTextSize(18);
             tv.setPadding(0, 5, 0, 5);
             convertView = tv;  
         }  
         TextView txt = (TextView) convertView;  
         txt.setText(mList.get(position));  
         return txt;  
	}
	
	private class MyFilter extends Filter {  
		  
        @Override  
        protected FilterResults performFiltering(CharSequence constraint) {  
            FilterResults results = new FilterResults();  
            if (mList == null) {  
                mList = new ArrayList<String>();  
            }  
            results.values = mList;  
            results.count = mList.size();  
            return results;  
        }  

        @Override  
        protected void publishResults(CharSequence constraint, FilterResults results) {  
            if (results.count > 0) {  
                notifyDataSetChanged();  
            } else {  
                notifyDataSetInvalidated();  
            }  
        }  
	}

}
