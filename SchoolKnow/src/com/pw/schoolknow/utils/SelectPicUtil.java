package com.pw.schoolknow.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class SelectPicUtil {
	
	// 将图片内容解析成字节数组
	public static byte[] readStream(InputStream inStream) throws Exception {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();
        return data;

	}
	
	
	 // 将字节数组转换为ImageView可调用的Bitmap对象
	 public static Bitmap getPicFromBytes(byte[] bytes,BitmapFactory.Options opts) {
		 if (bytes != null){
			 if (opts != null)
                 return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,opts);
			 else
                 return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		 }     
		 return null;
	 }
	
}
