package com.pw.schoolknow.adapter;

import java.util.List;
import java.util.Map;

import com.lidroid.xutils.BitmapUtils;
import com.pw.schoolknow.R;
import com.pw.schoolknow.helper.BitmapHelper;
import com.pw.schoolknow.helper.MyTagHandler;
import com.pw.schoolknow.utils.TimeUtil;
import com.pw.schoolknow.widgets.EmotionBox;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class Comment2Adapter extends BaseAdapter {
	
	private List<Map<String,Object>> list;
	private LayoutInflater inflater;
	private Context context;
	
	public BitmapUtils headBm;
	
	public Comment2Adapter(Context context,List<Map<String,Object>> list){
		this.list=list;
		this.context=context;
		this.inflater = LayoutInflater.from(context);
		headBm=BitmapHelper.getHeadBitMap(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		Map<String,Object> map=list.get(position);
		if (convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_comment_2, null, false);
			holder.name=(TextView) convertView.findViewById(R.id.comment_2_nick);
			holder.time=(TextView) convertView.findViewById(R.id.comment_2_time);
			holder.content=(TextView) convertView.findViewById(R.id.comment_2_content);
			holder.head=(ImageView) convertView.findViewById(R.id.comment_2_head);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		String str_name=map.get("nick").toString();
		if(str_name.trim().length()==0){
			holder.name.setText("ƒ‰√˚”√ªß");
		}else{
			holder.name.setText(str_name);
		}
		
		holder.time.setText(TimeUtil.getCommentTime(Long.parseLong(map.get("time").toString())));
		
		BitmapHelper.setHead(headBm, holder.head, map.get("uid").toString());
		
		CharSequence text=Html.fromHtml(map.get("content").toString(),null,
				new MyTagHandler(context,map.get("content").toString()));
		holder.content.setText(text);
		holder.content.setMovementMethod(LinkMovementMethod.getInstance());
		
		return convertView;
	}
	
	static class ViewHolder {
		ImageView head;
		TextView name;
		TextView time;
		TextView content;
	}

}
