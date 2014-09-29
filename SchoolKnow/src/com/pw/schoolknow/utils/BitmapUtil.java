package com.pw.schoolknow.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.ThumbnailUtils;

public class BitmapUtil {
	
	public static  byte[] Bitmap2Bytes(Bitmap bm){  
         ByteArrayOutputStream baos = new ByteArrayOutputStream();    
         bm.compress(Bitmap.CompressFormat.PNG, 100, baos);   
         return baos.toByteArray();  
     } 
	
	
	public static  Bitmap Bytes2Bimap(byte[] b){  
         if(b.length!=0){    
             return BitmapFactory.decodeByteArray(b, 0, b.length);  
         }  
         else {  
             return null;  
         }  
   }  
	
	public static String saveImg(Bitmap b,String path,String name) throws Exception{
        File mediaFile = new File(path+name);
        if(mediaFile.exists()){
                mediaFile.delete();
                 
        }
        if(!new File(path).exists()){
                new File(path).mkdirs();
        }
        mediaFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(mediaFile);
        b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        fos.flush();
        fos.close();
        b.recycle();
        b = null;
        System.gc();
        return mediaFile.getPath();
	}
	
	
	public static Bitmap loadImageFromUrl(String url, int sc)
    {

        URL m;
        InputStream i = null;
        BufferedInputStream bis = null;
        ByteArrayOutputStream out = null;

        if (url == null)
            return null;
        try
        {
            m = new URL(url);
            i = (InputStream) m.getContent();
            bis = new BufferedInputStream(i, 1024 * 4);
            out = new ByteArrayOutputStream();
            int len = 0;
            byte[] isBuffer=new byte[1024];

            while ((len = bis.read(isBuffer)) != -1)
            {
                out.write(isBuffer, 0, len);
            }
            out.close();
            bis.close();
        } catch (MalformedURLException e1)
        {
            e1.printStackTrace();
            return null;
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        if (out == null)
            return null;
        byte[] data = out.toByteArray();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        options.inJustDecodeBounds = false;
        int be = (int) (options.outHeight / (float) sc);
        if (be <= 0){
            be = 1;
        } else if (be > 3){
            be = 3;
        }
        options.inSampleSize = be;
        Bitmap bmp =null;
        try{
            bmp = BitmapFactory.decodeByteArray(data, 0, data.length, options); //返回缩略图
        } catch (OutOfMemoryError e){    
            System.gc();
            bmp =null;
        }
        return bmp;
    }
	
	
	public static Bitmap getImageThumbnail(String imagePath, int width, int height) {
		  Bitmap bitmap = null;
		  BitmapFactory.Options options = new BitmapFactory.Options();
		  options.inJustDecodeBounds = true;
		  // 获取这个图片的宽和高，注意此处的bitmap为null
		  bitmap = BitmapFactory.decodeFile(imagePath, options);
		  options.inJustDecodeBounds = false; // 设为 false
		  // 计算缩放比
		  int h = options.outHeight;
		  int w = options.outWidth;
		  int beWidth = w / width;
		  int beHeight = h / height;
		  int be = 1;
		  if (beWidth < beHeight) {
		   be = beWidth;
		  } else {
		   be = beHeight;
		  }
		  if (be <= 0) {
		   be = 1;
		  }
		  options.inSampleSize = be;
		  // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
		  bitmap = BitmapFactory.decodeFile(imagePath, options);
		  // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
		  bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
		    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		  return bitmap;
	}
	
	
	/**
	 *  将图片进行切图处理
	 * @param srcBitmap
	 * @param limitWidth
	 * @param limitHeight
	 * @return
	 */
	 public static Bitmap cutterBitmap(Bitmap srcBitmap, int limitWidth,
             int limitHeight) {
	     float width = srcBitmap.getWidth();
	     float height = srcBitmap.getHeight();
	
	
	     float limitScale = limitWidth / limitHeight;
	     float srcScale = width / height;
	     
	     if(limitScale>srcScale){
	             //高小了，所以要去掉多余的高度
	             height=limitHeight/((float)limitWidth/width);
	     }else{
	             //宽度小了，所以要去掉多余的宽度
	             width=limitWidth/((float)limitHeight/height);
	     }
	
	
	     Bitmap bitmap = Bitmap.createBitmap((int) width, (int) height,
	                     Config.ARGB_8888);
	     Canvas canvas = new Canvas(bitmap);
	     canvas.drawBitmap(srcBitmap, 0, 0, new Paint());
	     return bitmap;
	 }
	
}
