package com.pw.schoolknow.adapter;

import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapCommonUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.callback.SimpleBitmapLoadCallBack;
import com.pw.schoolknow.R;
import com.pw.schoolknow.utils.ViewHolder;
import com.pw.schoolknow.widgets.QImageView;
import com.pw.schoolknow.widgets.RoundProgressBar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * Gallery的适配器类，主要用于加载图片
 * @author lyc
 *
 */
public class GalleryAdapter extends BaseAdapter {

	private Context context;
	private TextView tv;
	public List<String> list;
	BitmapUtils bitmapUtils;
	BitmapLoadCallBack<QImageView> callback;
	/*图片素材*/

	public GalleryAdapter(Context context,TextView tv,List<String> list) {
		this.context = context;
		this.tv=tv;
		this.list=list;
		bitmapUtils = new BitmapUtils(context);
		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
		bitmapUtils.configDefaultLoadingImage(R.drawable.url_image_loading);
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.url_image_loading);
		bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(context));
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
		//每次移动获取图片并重新加载，当图片很多时可以构造函数就把bitmap引入并放入list当中，
		if (convertView == null) {
	        convertView = LayoutInflater.from(context)
	          .inflate(R.layout.item_photoview, parent, false);
	    }
		 tv.setText((position+1)+"/"+(list.size()));
		QImageView img = ViewHolder.get(convertView, R.id.item_photoview_img);
		final RoundProgressBar pb=ViewHolder.get(convertView, R.id.item_photoview_pb);
		pb.setMax(100);
		//ImageManager2.from(context).displayImage(img, list.get(position), R.drawable.default_img_load);
        BitmapLoadCallBack<QImageView> callback = new SimpleBitmapLoadCallBack<QImageView>() {
			public void onLoadCompleted(QImageView container, String uri,
					Bitmap bitmap, BitmapDisplayConfig config,BitmapLoadFrom from) {
				super.onLoadCompleted(container, uri, bitmap, config, from);
				pb.setProgress(100);
				pb.setVisibility(View.GONE);
				container.setImageBitmap(bitmap);
			}
			public void onLoadFailed(QImageView container, String uri,
					Drawable drawable) {
				super.onLoadFailed(container, uri, drawable);
				pb.setVisibility(View.GONE);
			}
			public void onLoading(QImageView container, String uri,
					BitmapDisplayConfig config, long total, long current) {
				super.onLoading(container, uri, config, total, current);
				int precent=(int)(current*100/total);
				pb.setProgress(precent);
			} 
        };
        
        bitmapUtils.display(img, list.get(position),callback);
		return convertView;
	}

}
