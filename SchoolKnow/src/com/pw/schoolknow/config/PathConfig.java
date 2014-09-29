package com.pw.schoolknow.config;

import android.os.Environment;

public class PathConfig {
	
	//sd卡路径
	public static final String BASEPATH=Environment.getExternalStorageDirectory()
			.getPath() + "/schoolknow";
	
	//保存头像
	public static final String HEADPATH=BASEPATH+"/data/head/";
	
	//保存下载的图片
   public static final String SavePATH=PathConfig.BASEPATH + "/" + "photo" + "/";
   
   //图片缓存地址
   public static final String CacheImgPATH=PathConfig.BASEPATH + "/data/imgCache";
   
   //临时文件夹
   public static final String TempPATH=PathConfig.BASEPATH + "/data/temp";
   
   //下载文件夹
   public static final String DownPATH=PathConfig.BASEPATH + "/DownLoad";
   
   // 检测sd是否可用
   public static boolean checkSD(){
	   String sdStatus = Environment.getExternalStorageState();
       if (sdStatus.equals(Environment.MEDIA_MOUNTED)) { 
             return true;
       }
	   return false;
   }
   
   
   
   
}
