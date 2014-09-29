package com.pw.schoolknow.widgets;

import java.util.List;

import com.pw.schoolknow.R;
import com.pw.schoolknow.utils.ViewHolder;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

public class MyTopPopMenu {
	
	public Context context;
	private GridView gv;
	private PopupWindow popupWindow;
	
	public interface MyTopPopMenuImp {
		public void onItemClick(int index,String value);
	}
	private MyTopPopMenuImp listener;
	private MyTopMenuAdapter adapter;
	
	
	public MyTopPopMenu(Context context,List<String> list) {
		
		this.context = context;
		
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View content=inflater.inflate(R.layout.wg_my_top_pop_menu, null);
		gv=(GridView) content.findViewById(R.id.wg_top_pop_grid);
		popupWindow = new PopupWindow(content, 
				 LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT, true);
		popupWindow.setBackgroundDrawable(new ColorDrawable(-00000));
		
		adapter=new MyTopMenuAdapter(context,list);
		gv.setAdapter(adapter);
		gv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				String value=adapter.getItem(position).toString();
				if (listener != null) {
					listener.onItemClick(position,value);
				}
				dismiss();
			}
		});
	}
	
	//接口传递事件
	public void setOnItemClick(MyTopPopMenuImp listener){
		this.listener=listener;
	}
	
	
	
	public void showAsDropDown(View parent) {
		popupWindow.showAsDropDown(parent,0,0);	
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();
	}
	
	public void dismiss() {
		popupWindow.dismiss();
	}
	
	
	/**
	 * 上弹出菜单Adapter
	 * @author peng
	 *
	 */
	class MyTopMenuAdapter extends BaseAdapter{
		
		private Context context;
		private List<String> list;
		
		public MyTopMenuAdapter(Context context, List<String> list) {
			this.context = context;
			this.list = list;
		}

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
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
		        convertView = LayoutInflater.from(context)
		          .inflate(R.layout.item_my_top_popmenu_gv, parent, false);
		    }
			TextView text=ViewHolder.get(convertView, R.id.item_my_top_popmenu_tv);
			text.setText(list.get(position));
			return convertView;
		}
	}

}
