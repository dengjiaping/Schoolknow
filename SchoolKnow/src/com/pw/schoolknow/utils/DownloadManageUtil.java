package com.pw.schoolknow.utils;

import java.io.File;
import java.net.URLDecoder;

import com.pw.schoolknow.helper.TipHelper;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
/**
 * 下载类
 * @author shiquanL
 *
 */
@SuppressLint("DefaultLocale")
public class DownloadManageUtil {
	static String FilePath;
	static BroadcastReceiver receiver;
	/**
	 * 
	 * @param context 上下文场景
	 * @param url 下载文件的地址
	 * @param path SD卡保存的路径 如："/MyDownload",自动在SD下创建该目录。
	 */
	public static void DownloadFile(Context context,String url,String path){

		/*
		 * 注册广播监听下载完成
		 */
		receiver = new DownloadCompleteReceiver();
		context.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
		/**
		 * 先检测SD卡是否存在
		 */
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return;
		}
		/*
		 * 创建文件夹
		 */
		String file = Environment.getExternalStorageDirectory().getPath() +path;
		File files = new File(file);
		if (files == null || !files.exists()) {
			files.mkdir();
		}
		/*
		 * 截取文件名
		 */
		String fileName = url.substring(url.lastIndexOf("/") + 1);
		fileName = URLDecoder.decode(fileName);
		/*
		 *系统下载服务类
		 */
		DownloadManager downManager = (DownloadManager)context.getSystemService(Activity.DOWNLOAD_SERVICE);
		DownloadManager.Request down = new DownloadManager.Request(Uri.parse(url));
		down.setShowRunningNotification(true);
		//在通知栏显示
		down.setVisibleInDownloadsUi(true);
		//输出目录
		down.setDestinationInExternalPublicDir(path+"/",fileName);
		//文件路径
		FilePath = file + "/" + fileName;
		//加入下载队列执行
		downManager.enqueue(down);
	}
	public static void unregisterReceiver(Context context){
		context.unregisterReceiver(receiver);
	};
	/**
	 * 监听下载完成
	 * @author Administrator
	 *
	 */
	public static class DownloadCompleteReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
				//获取文件路径
				//File files = new File(FilePath);
				//打开这个文件
				//Intent openFile = getFileIntent(files);
				//context.startActivity(openFile);
				TipHelper.PlaySound(context);
				T.showLong(context, "下载完成,文件保存在SD卡schoolknow文件夹中");
			}
		}
	}
	public static Intent getFileIntent(File file) {
		Uri uri = Uri.fromFile(file);
		String type = getMIMEType(file);
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(uri, type);
		return intent;
	}
	@SuppressLint("DefaultLocale")
	private static String getMIMEType(File f){   
	      String type="";  
	      String fName=f.getName();  
	      /* 取得扩展名 */  
	      String end=fName.substring(fName.lastIndexOf(".")+1,fName.length()).toLowerCase();
	      
	      /* 依扩展名的类型决定MimeType */
	      if(end.equals("pdf")){
	              type = "application/pdf";//
	      }
	      else if(end.equals("m4a")||end.equals("mp3")||end.equals("mid")||  
	      end.equals("xmf")||end.equals("ogg")||end.equals("wav")){  
	        type = "audio/*";   
	      }  
	      else if(end.equals("3gp")||end.equals("mp4")){  
	        type = "video/*";  
	      }  
	      else if(end.equals("jpg")||end.equals("gif")||end.equals("png")||  
	      end.equals("jpeg")||end.equals("bmp")){  
	        type = "image/*";  
	      }  
	      else if(end.equals("apk")){   
	        type = "application/vnd.android.package-archive"; 
	      }
	      else{
//	              /*如果无法直接打开，就跳出软件列表给用户选择 */  
	        type="*/*";
	      }
	      return type;
	    }
}
