package com.pw.schoolknow.widgets;




import com.pw.schoolknow.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 仿QQ弹出菜单
 * @author peng
 *
 */


public class MyDialogMenu {
	
	public Context mcontext;
	public Dialog dialog;
	public String[] param;
	public Button btn;
	public ListView lv;
	
	public OnItemClickCallBack callBack;
	
	public interface OnItemClickCallBack{
		public void OnItemClick(View view,int position);
	}
	
	public MyDialogMenu(Context context,String param[]){
		this.mcontext=context;
		this.param=param;
		
		//设置菜单内容
		View layout = LayoutInflater.from(context).inflate(R.layout.wg_dialog_menu, null);
		btn=(Button) layout.findViewById(R.id.wg_dialog_menu_btn);
		lv=(ListView) layout.findViewById(R.id.wg_dialog_menu_lv);
		if(param.length>=1){
			btn.setText(param[0]);
			lv.setAdapter(new MyDialogMenuAdapter(context, param));
		}
		
		/**
		 * 事件传递
		 */
		btn.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				callBack.OnItemClick(view,0);
			}
		});
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				callBack.OnItemClick(view,position+1);
			}
		});
		
		
		//设置弹出框属性
		dialog = new Dialog(context, R.style.transparentFrameWindowStyle);
		dialog.setContentView(layout, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		Window window = dialog.getWindow();
		// 设置显示动画
		window.setWindowAnimations(R.style.main_menu_animstyle);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		wl.y = wm.getDefaultDisplay().getHeight();
		
		// 以下这两句是为了保证按钮可以水平满屏
		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
		wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

		// 设置显示位置
		dialog.onWindowAttributesChanged(wl);
		
		// 设置点击外围解散
		//dialog.setCanceledOnTouchOutside(true);
		
		dialog.show();
	}
	
	
	/**
	 * 事件绑定
	 * @param callBack
	 */
	public void setOnItemClick(OnItemClickCallBack callBack){
		this.callBack=callBack;
	}
	
	public void dismiss(){
		dialog.dismiss();
	}
	
	public void show(){
		dialog.show();
	}
	
	
	/**
	 * 界面adapter
	 * @author peng
	 *
	 */
	public class MyDialogMenuAdapter extends BaseAdapter{
		
		public Context context;
		public String param[];
		private LayoutInflater inflater;

		public MyDialogMenuAdapter(Context context, String[] param) {
			this.context = context;
			this.param = param;
			this.inflater = LayoutInflater.from(context);
		}

		public int getCount() {
			return param.length-1;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return param[position+1];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null){
				holder = new ViewHolder();
				convertView=inflater.inflate(R.layout.wg_dialog_menu_lv_item, null, false);
				holder.btn=(TextView) convertView.findViewById(R.id.wg_dialog_menu_item_btn);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.btn.setText(param[position+1]);
			if(getCount()==1){
				holder.btn.setBackgroundResource(R.drawable.photo_cancel_selector);
			}else if(getCount()==2){
				if(position==0){
					holder.btn.setBackgroundResource(R.drawable.photo_gallery_selector);
				}else{
					holder.btn.setBackgroundResource(R.drawable.photo_camera_selector);
				}
			}else if(getCount()>=3){
				if(position==0){
					holder.btn.setBackgroundResource(R.drawable.photo_gallery_selector);
				}else if(position==getCount()-1){
					holder.btn.setBackgroundResource(R.drawable.photo_camera_selector);
				}else{
					holder.btn.setBackgroundResource(R.drawable.photo_other_selector);
				}
			}
			return convertView;
		}
	}
	static class ViewHolder {
		TextView btn;
	}
}
