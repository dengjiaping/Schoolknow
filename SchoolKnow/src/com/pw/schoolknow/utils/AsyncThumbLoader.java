package com.pw.schoolknow.utils;

import java.lang.ref.SoftReference;
import java.util.HashMap;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

@SuppressLint("HandlerLeak")
public class AsyncThumbLoader {

	 private HashMap<String, SoftReference<Drawable>> imageCache;
	 
	 //略缩图缩小倍数
	 public int precent;
	 
	  
     public AsyncThumbLoader() {
    	 imageCache = new HashMap<String, SoftReference<Drawable>>();
    	 precent=10;
     }
     
     public AsyncThumbLoader(int precent){
    	 imageCache = new HashMap<String, SoftReference<Drawable>>();
    	 this.precent=precent;
     }
	  
     public Drawable loadDrawable(final String imageUrl, final ImageCallback imageCallback) {
    	 
    	 //final String fileName=new Sha1Util().getDigestOfString(imageUrl.getBytes());
    	 //String FileUrl=PathConfig.CacheImgPATH+"/"+fileName;
    	 
    	 //从内存读出
         if (imageCache.containsKey(imageUrl)) {
             SoftReference<Drawable> softReference = imageCache.get(imageUrl);
             Drawable drawable = softReference.get();
             if (drawable != null) {
                 return drawable;
             }
         }
         
         //从内存卡读出
//	         if(new File(FileUrl).exists()){
//	        	 Bitmap bm=BitmapFactory.decodeFile(FileUrl);
//	        	 if(bm!=null){
//	        		 return new BitmapDrawable(bm);
//	        	 }
//	         }
         
         //异步下载
         final Handler handler = new Handler() {
             public void handleMessage(Message message) {
            	
                 imageCallback.imageLoaded((Drawable) message.obj, imageUrl);
             }
         };
         
         new Thread() {
             @Override
             public void run() {
            	 Bitmap bm=BitmapUtil.loadImageFromUrl(imageUrl,precent);
                 Drawable drawable = new BitmapDrawable(bm);
                 imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
                 Message message = handler.obtainMessage(0, drawable);
                 handler.sendMessage(message);
             }
         }.start();
         return null;
     }
  
     public interface ImageCallback {
         public void imageLoaded(Drawable imageDrawable, String imageUrl);
     }

}
