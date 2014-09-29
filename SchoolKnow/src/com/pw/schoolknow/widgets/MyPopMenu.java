package com.pw.schoolknow.widgets;

import java.util.ArrayList;

import com.pw.schoolknow.R;
import com.pw.schoolknow.helper.SystemHelper;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;



public class MyPopMenu implements OnItemClickListener {
	
	public interface MyPopMenuImp {
		public void onItemClick(int index);
	}
	
	private MyPopMenuImp listener;
	
	private ArrayList<String> itemList;
	private Context context;
	private PopupWindow popupWindow;
	private ListView listView;
	private LayoutInflater inflater;
	private LinearLayout layout;

	
	public MyPopMenu(Context context) {
		this.context = context;

		itemList = new ArrayList<String>(5);

		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.wg_pop_menu, null);

		listView = (ListView) view.findViewById(R.id.mypopmenu_listView);
		layout=(LinearLayout) view.findViewById(R.id.mypopmenu_layout);
		
		listView.setAdapter(new PopAdapter());
		listView.setOnItemClickListener(this);

		popupWindow = new PopupWindow(view, 
				SystemHelper.getScreenWidth(context)/2,  //这里宽度需要自己指定，使用 WRAP_CONTENT 会很大
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景（很神奇的）
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (listener != null) {
			listener.onItemClick(position);
		}
		dismiss();
	}

	// 设置菜单项点击监听器
	public void setOnItemClickListener(MyPopMenuImp listener) {
		 this.listener = listener;
	}

	// 批量添加菜单项
	public void addItems(String[] items) {
		for (String s : items)
			itemList.add(s);
	}

	// 单个添加菜单项
	public void addItem(String item) {
		itemList.add(item);
	}

	// 下拉式 弹出 pop菜单 parent 右下角
	public void showAsDropDown(View parent) {
		popupWindow.showAsDropDown(parent, 10,
		// 保证尺寸是根据屏幕像素密度来的
				context.getResources().getDimensionPixelSize(R.dimen.popmenu_yoff));		
		// 使其聚集
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		// 刷新状态
		popupWindow.update();
	}
	
	/**
	 * 显示在正中间
	 * @param v
	 */
	public void showCenter(View parent){
		int xoff = popupWindow.getWidth()/2-parent.getWidth()/2;
		popupWindow.showAsDropDown(parent, -xoff, 0);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();
		setStyleCenter();
		
	}
	
	
	/**
	 * 设置样式
	 * 分别为:尖角在左边，右边和中间
	 */
	public void setStyleRight(){
		layout.setBackgroundDrawable(context.getResources().getDrawable
				(R.drawable.bg_popmenu_right));
	}
	
	public void setStyleLeft(){
		layout.setBackgroundDrawable(context.getResources().getDrawable
				(R.drawable.bg_popmenu_left));
	}
	
	public void setStyleCenter(){
		layout.setBackgroundDrawable(context.getResources().getDrawable
				(R.drawable.bg_popmenu_center));
	}
	
	

	// 隐藏菜单
	public void dismiss() {
		popupWindow.dismiss();
	}

	// 适配器
	private final class PopAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return itemList.size();
		}

		@Override
		public Object getItem(int position) {
			return itemList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_popmenu,parent, false);
				holder = new ViewHolder();
				convertView.setTag(holder);
				holder.groupItem = (TextView) convertView.findViewById(R.id.mypopmenu_textView);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.groupItem.setText(itemList.get(position));

			return convertView;
		}

		private final class ViewHolder {
			TextView groupItem;
		}
	}
}