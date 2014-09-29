package com.pw.schoolknow.adapter;

import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapCommonUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.core.BitmapSize;
import com.pw.schoolknow.R;
import com.pw.schoolknow.utils.BitmapUtil;
import com.pw.schoolknow.utils.ViewHolder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class SFImgShowAdapter extends BaseAdapter {
	
	private Context context;
	private  List<String> list;
	public GridView gv;
	private int ResId;
	BitmapUtils bitmapUtils;
	
	public SFImgShowAdapter(Context context, List<String> list,GridView gv,int ResId) {
		this.context = context;
		this.list = list;
		this.gv=gv;
		this.ResId=ResId;
		bitmapUtils = new BitmapUtils(context);
		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
		bitmapUtils.configDefaultLoadingImage(R.drawable.img_square_load_preview);
		//int img_width=SystemHelper.getScreenWidth(context)/6;
		BitmapSize bs=BitmapCommonUtils.getScreenSize(context);
		bitmapUtils.configDefaultBitmapMaxSize(bs.scaleDown(3));
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
	          .inflate(ResId, parent, false);
	    }
		ImageView img = ViewHolder.get(convertView, R.id.gv_item_img);
		String url=list.get(position);
		
		//略缩图设置为屏幕长度的1/6
		
		//ImageManager2.from(context).displayImage(img, url, R.drawable.img_square_load_preview,img_width,img_width);
	    
	    BitmapLoadCallBack<ImageView> callback=new BitmapLoadCallBack<ImageView>() {
			public void onLoadFailed(ImageView arg0, String arg1, Drawable arg2) {}
			public void onLoadCompleted(ImageView container, String arg1, Bitmap bitmap,
					BitmapDisplayConfig arg3, BitmapLoadFrom arg4) {
				if(bitmap!=null&&list.size()>1){
					bitmap=BitmapUtil.cutterBitmap(bitmap, 100,100);
					container.setImageBitmap(bitmap);
				}else{
					container.setImageBitmap(bitmap);
				}
				
			}
			
		};
		bitmapUtils.display(img,url,callback);
		return convertView;
	}
	
	

}
