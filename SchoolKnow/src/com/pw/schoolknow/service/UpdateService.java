package com.pw.schoolknow.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.pw.schoolknow.R;
import com.pw.schoolknow.activity.Main;
import com.pw.schoolknow.config.PathConfig;
import com.pw.schoolknow.helper.VersionHelper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.RemoteViews;

public class UpdateService extends Service {
	
	private static final int NOTIFY_ID = 0;
	private int progress=0;
	private NotificationManager mNotificationManager;
	private String apkUrl = "";
	private static final String savePath = PathConfig.BASEPATH+"/updateApk/";
	private static final String saveFileName = savePath + "schoolknow.apk";
	private Context mContext;
	private Notification mNotification;
	private int lastRate = 0;
	private boolean canceled=false;
	
	private VersionHelper version;

	@Override
	public IBinder onBind(Intent it) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = this;
		version=new VersionHelper(this);
		apkUrl=version.getLoadUrl();
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		setUpNotification();
		new AsyncUpdate().execute("");
		
	}

	/**
	 * 创建通知
	 */
	private void setUpNotification() {
		int icon = R.drawable.ic_launcher;
		CharSequence tickerText ="校园通开始下载";
		long when = System.currentTimeMillis();
		mNotification = new Notification(icon, tickerText, when);
		// 放置在"正在运行"栏目中
		mNotification.flags = Notification.FLAG_ONGOING_EVENT;
		

		RemoteViews contentView = new RemoteViews(getPackageName(),
				R.layout.part_notify_update);
		contentView.setTextViewText(R.id.name, "校园通正在下载...");
		// 指定个性化视图
		mNotification.contentView = contentView;

		Intent intent = new Intent(this, Main.class);
		// 下面两句是 在按home后，点击通知栏，返回之前activity 状态;
		// 有下面两句的话，假如service还在后台下载， 在点击程序图片重新进入程序时，直接到下载界面，相当于把程序MAIN 入口改了 - -
		// 是这么理解么。。。
		// intent.setAction(Intent.ACTION_MAIN);
		// intent.addCategory(Intent.CATEGORY_LAUNCHER);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

		// 指定内容意图
		mNotification.contentIntent = contentIntent;	  
		mNotificationManager.notify(NOTIFY_ID, mNotification);
	}
	
	/**
	 * 安装APK
	 */
	private void installApk() {
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		version.clear();
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		mContext.startActivity(i);
	}
	
	class AsyncUpdate extends AsyncTask<String,Integer,String>{
		@Override
		protected String doInBackground(String... params) {
			try {
				URL url = new URL(apkUrl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();
				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdirs();
				}
				String apkFile = saveFileName;
				File ApkFile = new File(apkFile);
				FileOutputStream fos = new FileOutputStream(ApkFile);

				int count = 0;
				byte buf[] = new byte[1024];
				do{
					int numread = is.read(buf);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					// 更新进度
					if (progress >= lastRate + 1) {
						publishProgress(progress);
						lastRate = progress;
					}
					if (numread <= 0) {
						// 下载完成通知安装
						canceled = true;
						break;
					}
					fos.write(buf, 0, numread);
				}while(!canceled);
				
				fos.close();
				is.close();
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			mNotificationManager.cancel(NOTIFY_ID);
			installApk();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			int rate = values[0];
			if (rate < 100) {
				RemoteViews contentview = mNotification.contentView;
				contentview.setTextViewText(R.id.tv_progress, rate + "%");
				contentview.setProgressBar(R.id.progressbar, 100, rate,false);
			} else {
				// 下载完毕后变换通知形式
				mNotification.flags = Notification.FLAG_AUTO_CANCEL;
				mNotification.contentView = null;
				Intent intent = new Intent(mContext,
						Main.class);
				// 告知已完成
				intent.putExtra("completed", "yes");
				// 更新参数,注意flags要使用FLAG_UPDATE_CURRENT
				PendingIntent contentIntent = PendingIntent.getActivity(
						mContext, 0, intent,
						PendingIntent.FLAG_UPDATE_CURRENT);
				mNotification.setLatestEventInfo(mContext, "下载完成",
						"文件已下载完毕", contentIntent);
				stopSelf();// 停掉服务自身
			}
			mNotificationManager.notify(NOTIFY_ID, mNotification);
			super.onProgressUpdate(values);
		}
		
		
	}
	
	

}
