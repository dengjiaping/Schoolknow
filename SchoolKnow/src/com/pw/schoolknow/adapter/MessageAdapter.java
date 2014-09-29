package com.pw.schoolknow.adapter;

import java.util.List;
import java.util.regex.Pattern;




import com.lidroid.xutils.BitmapUtils;
import com.pw.schoolknow.R;
import com.pw.schoolknow.base.MessageItem;
import com.pw.schoolknow.helper.BitmapHelper;
import com.pw.schoolknow.utils.TimeUtil;
import com.pw.schoolknow.widgets.EmotionBox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class MessageAdapter extends BaseAdapter {
	
	private List<MessageItem> list;
	private Context context;
	private LayoutInflater mInflater;
	public static final Pattern EMOTION_URL = Pattern.compile("\\[(\\S+?)\\]");
	
	public BitmapUtils headBitmap;
	
	
	public MessageAdapter(Context context,List<MessageItem> list){
		this.list=list;
		this.context=context;
		mInflater = LayoutInflater.from(this.context);
		headBitmap=BitmapHelper.getHeadBitMap(context);
	}
	
	/**
	 * 发布聊天内容
	 * @param msg
	 */
	public void upDateMsg(MessageItem msg) {
		list.add(msg);
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
		
		final MessageItem item = list.get(position);
		ViewHolder holder = null;
		
		if (convertView == null||convertView.getTag(R.drawable.ic_launcher+position) == null){
			holder = new ViewHolder();
			if(item.isComMeg()){
				convertView = mInflater.inflate(R.layout.chat_item_left, null);
			}else{
				convertView = mInflater.inflate(R.layout.chat_item_right, null);
			}
			holder.head = (ImageView) convertView.findViewById(R.id.user_icon);
			holder.time = (TextView) convertView.findViewById(R.id.chat_message_time);
			holder.msg = (TextView) convertView.findViewById(R.id.chat_message_content);
			holder.img=(ImageView) convertView.findViewById(R.id.chat_message_img);
			convertView.setTag(R.drawable.ic_launcher + position);
		}else{
			holder = (ViewHolder) convertView.getTag(R.drawable.ic_launcher
					+ position);
		}
		String uid=item.getUid();
		BitmapHelper.setHead(headBitmap, holder.head, uid);
		
		//一分钟内的评论显示一次时间
		if(position>0){
			if(TimeUtil.getChatTime(item.getTime()).equals(TimeUtil.getChatTime(list.get(position-1).getTime()))){
				holder.time.setVisibility(View.GONE);
			}else{
				holder.time.setVisibility(View.VISIBLE);
			}
		}else{
			holder.time.setVisibility(View.VISIBLE);
		}
		holder.time.setText(TimeUtil.getChatTime(item.getTime()));
		switch(item.getMsgType()){
		case 1:
			holder.msg.setVisibility(View.VISIBLE);
			holder.img.setVisibility(View.GONE);
			holder.msg.setText(
					EmotionBox.convertNormalStringToSpannableString(context,item.getMessage()),BufferType.SPANNABLE);
			break;
		case 2:
			holder.img.setVisibility(View.VISIBLE);
			holder.msg.setVisibility(View.GONE);
			holder.img.setImageResource(Integer.parseInt(item.getMessage()));
			holder.img.setOnClickListener(new OnClickListener() {
				public void onClick(View view) {
					
				}
			});
			break;
		default:
			break;
		}
		return convertView;
	}
	
	static class ViewHolder {
		ImageView head;
		TextView time;
		TextView msg;
		ImageView img;
	}

}
