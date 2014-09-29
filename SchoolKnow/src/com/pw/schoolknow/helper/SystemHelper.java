package com.pw.schoolknow.helper;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.view.WindowManager;

public class SystemHelper {
	/**
	 * 获取屏幕的宽
	 * @return
	 */
	public static int getScreenWidth(Context context){
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getWidth();
	}
	
	/**
	 * 获取屏幕的高
	 * @return
	 */
	
	public static int getScreenHeight(Context context){
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getHeight();
	}
	
	/**
	 *  获得手机串口号码（IMEI）
	 * @param context
	 * @return
	 */
	public static String getTelephoneIMEI(Context context) {
        TelephonyManager telMg = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telMg.getDeviceId();
    }
	
	/**
	 * 获取手机号码
	 * @param context
	 * @return
	 */
	public static String getTelNum(Context context){
		TelephonyManager telMg = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telMg.getLine1Number();
		
	}
	
	/**
	 * 获取手机IP地址
	 * @return
	 */
	public static  String getLocalIpAddress() {
        try{
            for (Enumeration<NetworkInterface> en = NetworkInterface
                            .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                                .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                                return inetAddress.getHostAddress().toString();
                        }
                }
            }
        } catch (SocketException ex) {
           
        }
        return "error";
	}
	
	/**
	 * 获取手机品牌和型号
	 * @return
	 */
	public static String getPhoneInfo(Context context){
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String mtyb = android.os.Build.BRAND;// 手机品牌
		String mtype = android.os.Build.MODEL; // 手机型号
		String imsi = tm.getSubscriberId();   //手机串口号码（IMEI）
		return mtyb+"(type="+mtype+",imsi="+imsi+")";
		
	}
	
	/** 
	 * 根据手机的分辨率从 PX(像素) 的单位 转成为 DP
	 */
	 public static int dip2px(Context context, float dpValue) {        
	    	final float scale = context.getResources().getDisplayMetrics().density;        
	    	return (int) (dpValue * scale + 0.5f);} 
	 public static int px2dip(Context context, float pxValue) {        
	    	final float scale = context.getResources().getDisplayMetrics().density;        
	    	return (int) (pxValue / scale + 0.5f);}

}
