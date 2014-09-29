package com.pw.schoolknow.app;



import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import com.baidu.frontia.FrontiaApplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lidroid.xutils.BitmapUtils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.pw.schoolknow.config.PathConfig;
import com.pw.schoolknow.config.ServerConfig;
import com.pw.schoolknow.helper.BitmapHelper;
import com.pw.schoolknow.push.BaiduPush;
import com.pw.schoolknow.push.RestApi;
import com.pw.schoolknow.utils.FileUtils;

import android.content.Context;

public class MyApplication extends FrontiaApplication {
	
	
	private static MyApplication mApplication;
	public static String BaiduMapApi="esDimeNhrIZmnvB3N0hZu2lM";
	private BaiduPush mBaiduPushServer;
	
	private  BitmapUtils bitmapUtils;
	private HttpClient httpClient;
	
	
	private Gson mGson;
	
	public synchronized static MyApplication getInstance() {
		return mApplication;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		mApplication = this;
		
		//创建根目录
		FileUtils.createPath(PathConfig.BASEPATH);
		
		bitmapUtils=BitmapHelper.getBitmapUtils(mApplication);
		
		//进行异常捕获
		if(!ServerConfig.DEVELOP_MODE){
			CrashHandler crashHandler = CrashHandler.getInstance();  
	        crashHandler.init(this); 
		}
		
		httpClient = this.createHttpClient();
		initImageLoader(getApplicationContext());
		
	}
	
	public synchronized Gson getGson() {
		if (mGson == null)
			// 不转换没有 @Expose 注解的字段
			mGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
					.create();
		return mGson;
	}
	
	public synchronized BaiduPush getBaiduPush() {
		if (mBaiduPushServer == null)
			mBaiduPushServer = new BaiduPush(BaiduPush.HTTP_METHOD_POST,
					RestApi.mSecretKey,RestApi.mApiKey);
		return mBaiduPushServer;

	}

	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		bitmapUtils.clearMemoryCache();
		shutdownHttpClient();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		bitmapUtils.clearMemoryCache();
		shutdownHttpClient();
	}
	
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
	
	// 创建HttpClient实例
	private HttpClient createHttpClient() {
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params,
				HTTP.DEFAULT_CONTENT_CHARSET);
		HttpProtocolParams.setUseExpectContinue(params, true);
		HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
		HttpConnectionParams.setSoTimeout(params, 20 * 1000);
		HttpConnectionParams.setSocketBufferSize(params, 8192);
		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		//schReg.register(new Scheme("https",SSLSocketFactory.getSocketFactory(), 443));

		ClientConnectionManager connMgr = new ThreadSafeClientConnManager(
				params, schReg);

		return new DefaultHttpClient(connMgr, params);
	}
	
	// 关闭连接管理器并释放资源
	private void shutdownHttpClient() {
		if (httpClient != null && httpClient.getConnectionManager() != null) {
			httpClient.getConnectionManager().shutdown();
		}
	}

	// 对外提供HttpClient实例
	public HttpClient getHttpClient() {
		return httpClient;
	}


	
	
}
