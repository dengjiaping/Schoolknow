package com.pw.schoolknow.adapter;

import java.util.List;
import java.util.Map;

import com.lidroid.xutils.BitmapUtils;
import com.pw.schoolknow.R;
import com.pw.schoolknow.helper.BitmapHelper;
import com.pw.schoolknow.utils.PinyinUtils;
import com.pw.schoolknow.utils.ViewHolder;



import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



public class FriendListAdapter extends BaseAdapter {
	
	private Context context;
	private List<Map<String, Object>> list;
	private BitmapUtils headBitmap;
	
	public static final String[] FriendListTool={"推荐好友","添加好友","通讯录好友"};
	
	public FriendListAdapter(Context context, List<Map<String, Object>> list){
		this.context = context;
		this.list = list;
		headBitmap=BitmapHelper.getHeadBitMap(context);
	}

	@Override
	public int getCount() {
		return list == null ? FriendListTool.length : list.size()+FriendListTool.length; 
	}

	@Override
	public Object getItem(int position) {
		return list.get(position-FriendListTool.length);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("DefaultLocale")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHold holder = null;
		
		if(position>=FriendListTool.length){
//			if (convertView == null||position==FriendListTool.length||((ViewHold)convertView.getTag()).catalog==null) {
//				holder = new ViewHold();
//				convertView = LayoutInflater.from(context).inflate(R.layout.item_friends_list_lv, null);
//				holder.catalog=(TextView) convertView.findViewById(R.id.catalogTv);
//				holder.icon = (ImageView) convertView.findViewById(R.id.right_slide_menu_friend_img);
//				holder.name=(TextView) convertView.findViewById(R.id.right_slide_menu_friend_name);
//				holder.nickname=(TextView) convertView.findViewById(R.id.right_slide_menu_friend_nickname);
//				holder.mood=(TextView) convertView.findViewById(R.id.right_slide_menu_friend_mood);
//				convertView.setTag(holder);
//			}else{
//				holder = (ViewHold) convertView.getTag();
//			}
			holder = new ViewHold();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_friends_list_lv, null);
			holder.catalog=(TextView) convertView.findViewById(R.id.catalogTv);
			holder.icon = (ImageView) convertView.findViewById(R.id.right_slide_menu_friend_img);
			holder.name=(TextView) convertView.findViewById(R.id.right_slide_menu_friend_name);
			holder.nickname=(TextView) convertView.findViewById(R.id.right_slide_menu_friend_nickname);
			holder.mood=(TextView) convertView.findViewById(R.id.right_slide_menu_friend_mood);
			convertView.setTag(holder);
			
			Map<String, Object> map=list.get(position-FriendListTool.length);
			String catalog = PinyinUtils.converterToFirstSpell(map.get("nickname").toString()).substring(0, 1);
			if (position == FriendListTool.length) {
				holder.catalog.setVisibility(View.VISIBLE);
				holder.catalog.setText(catalog);
			}else{
				String lastCatalog = PinyinUtils.converterToFirstSpell(list.get(position - 1-FriendListTool.length)
						.get("nickname").toString()).substring(0, 1);
				if (catalog.equals(lastCatalog)||catalog.equals(lastCatalog.toLowerCase())) {
					holder.catalog.setVisibility(View.GONE);
				} else {
					holder.catalog.setVisibility(View.VISIBLE);
					holder.catalog.setText(catalog);
					
				}
			}
			
			
			BitmapHelper.setHead(headBitmap,holder.icon,(String) map.get("uid"));
			holder.name.setText((String) map.get("name"));
			holder.nickname.setText((String) map.get("nickname"));
			holder.mood.setText((String) map.get("mood"));
		}else{
//			if (convertView == null) {
//		        convertView = LayoutInflater.from(context)
//		          .inflate(R.layout.item_friend_list_lv_2, parent, false);
//		    }
			convertView = LayoutInflater.from(context)
			          .inflate(R.layout.item_friend_list_lv_2, parent, false);
			
			TextView title = ViewHolder.get(convertView, R.id.firend_list_tool_text);
			Drawable drawable=null;
			switch(position){
			case 0:
				drawable = context.getResources().getDrawable(R.drawable.icon_contact_group); 	
				break;
			case 1:
				drawable = context.getResources().getDrawable(R.drawable.icon_contact_publicplatform); 	
				break;
			case 2:
				drawable = context.getResources().getDrawable(R.drawable.icon_contact_helper); 	
				break;
			default:
				drawable = context.getResources().getDrawable(R.drawable.icon_contact_group); 	
				break;
			}
			drawable.setBounds(0, 0, 70, 70); 
			title.setCompoundDrawables(drawable, null, null, null);
			title.setText(FriendListTool[position]);
		}
		
		
		return convertView;
	}
	
	public final class ViewHold {
		
		public TextView catalog;
		public ImageView icon;
		public TextView name;
		public TextView nickname;
		public TextView mood;
		public LinearLayout divline;
	}

}
