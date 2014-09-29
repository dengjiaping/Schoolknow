package com.pw.schoolknow.helper;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.util.core.LruDiskCache.DiskCacheFileNameGenerator;
import com.pw.schoolknow.R;
import com.pw.schoolknow.app.MyApplication;
import com.pw.schoolknow.config.DecodeConfig;
import com.pw.schoolknow.config.PathConfig;
import com.pw.schoolknow.utils.ImageUtil;
import com.pw.schoolknow.utils.Sha1Util;



/**
 * 管理BitmapUtils
 * @author wei8888go
 *
 */
public class BitmapHelper {
	
	private static BitmapUtils bitmapUtils;
	private static BitmapUtils headBitmap;
	
	private BitmapHelper() {}

    /**
     * BitmapUtils不是单例的 根据需要重载多个获取实例的方法
     * @param appContext application context
     * @return
     */
    public static BitmapUtils getBitmapUtils(Context appContext) {
        if (bitmapUtils == null) {
            bitmapUtils = new BitmapUtils(appContext);
        }
        return bitmapUtils;
    }
    
    
    /**
     * 头像设置
     * @param appContext
     * @return
     */
    public static BitmapUtils getHeadBitMap(final Context appContext){
    	if (headBitmap == null) {
    		if(PathConfig.checkSD()){
    			headBitmap = new BitmapUtils(appContext,PathConfig.HEADPATH);
    		}else{
    			headBitmap = new BitmapUtils(appContext);
    		}
        }
    	headBitmap.configDiskCacheEnabled(true);
    	headBitmap.configDiskCacheFileNameGenerator(new DiskCacheFileNameGenerator() {
			public String generate(String fileName) {
				return Sha1Util.encode(fileName);
			}
		});
    	headBitmap.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
    	headBitmap.configDefaultLoadingImage(R.drawable.default_head);
        return headBitmap;
    }
    
    /**
     * 头像配置
     */
    public static BitmapLoadCallBack<ImageView> headCallback=new BitmapLoadCallBack<ImageView>() {
		public void onLoadCompleted(ImageView head, String arg1, Bitmap bm,
				BitmapDisplayConfig arg3, BitmapLoadFrom arg4) {
			head.setImageBitmap(ImageUtil.toRoundCorner(bm, 10));
		}
		public void onLoadFailed(ImageView head, String arg1, Drawable arg2) {
			head.setImageResource(R.drawable.default_head);
		}
	};
	
	/**
	 * 设置头像
	 */
	public static void setHead(BitmapUtils bitmapUtils,ImageView img,String uid){
		setHead(bitmapUtils,img,uid,BitmapHelper.headCallback);
	}
	public static void setHead(BitmapUtils bitmapUtils,ImageView img,String uid,BitmapLoadCallBack<ImageView> headCallback){
		try{
			String headUrl=DecodeConfig.getHeadUrlById(uid);
			File tempFile=bitmapUtils.getBitmapFileFromDiskCache(headUrl);
			if(tempFile!=null&&tempFile.exists()){
				bitmapUtils.display(img,tempFile.getPath(),headCallback);
			}else{
				if(NetHelper.isNetConnected(MyApplication.getInstance())){
					bitmapUtils.display(img, headUrl,headCallback);
				}else{
					img.setImageResource(R.drawable.default_head);
				}
			}
		}catch(Exception e){
			img.setImageResource(R.drawable.default_head);
		}
	}
    
}
