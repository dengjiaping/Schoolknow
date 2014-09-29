package com.pw.schoolknow.adapter;

import java.util.List;
import java.util.Map;

import com.pw.schoolknow.R;
import com.pw.schoolknow.utils.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AskRankingAdapter extends BaseAdapter {
	
	private List<Map<String,Object>> list;
	private Context context;
	
	public AskRankingAdapter(List<Map<String, Object>> list, Context context) {
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

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
	        convertView = LayoutInflater.from(context)
	          .inflate(R.layout.item_ask_ranking_lv, parent, false);
	    }
		TextView rank = ViewHolder.get(convertView,R.id.ask_ranking_item_num);
		TextView name = ViewHolder.get(convertView,R.id.ask_ranking_item_name);
		TextView score = ViewHolder.get(convertView,R.id.ask_ranking_item_score);
		
		Map<String,Object> map=list.get(position);
		
		rank.setText(map.get("rank").toString());
		name.setText(map.get("name").toString());
		score.setText(map.get("score").toString());
		
		switch(position){
		case 0:
			rank.setBackgroundResource(R.drawable.rank_first);
			rank.setPadding(0, 0, 0, 0);
			break;
		case 1:
			rank.setBackgroundResource(R.drawable.rank_second);
			rank.setPadding(0, 0, 0, 0);
			break;
		case 2:
			rank.setBackgroundResource(R.drawable.rank_third);
			rank.setPadding(0, 0, 0, 0);
			break;
		default:
			rank.setBackgroundDrawable(null);
			rank.setPadding(0, 0, 0, 0);
			break;
		}
		
		return convertView;
	}

}
