package com.pw.schoolknow.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class GetUtil {
	
	/**
	 * 从链接中获取资源
	 * @param url
	 * @return
	 */
	public static String getRes(String url){
		try {
			URL u=new URL(url);
			HttpURLConnection conn=(HttpURLConnection) u.openConnection();
			
			//设置超时
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(30000);
			 StringBuffer buffer = new StringBuffer();
			 InputStreamReader r = new InputStreamReader(conn.getInputStream());
			 BufferedReader rd = new BufferedReader(r);
			 String line;
			 while ((line = rd.readLine()) != null) {
				    buffer.append(line);
			 }
			 rd.close();
			 conn.getInputStream().close();
			 return buffer.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
	
	
	/**
	 * 获取链接图片
	 * @return
	 */
	
	public static Bitmap getBitMap(String picUrl){
		URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
        	myFileUrl = new URL(picUrl);
	        HttpURLConnection conn = (HttpURLConnection) myFileUrl
	                        .openConnection();
	        conn.setDoInput(true);
	        conn.connect();
	        InputStream is = conn.getInputStream();
	        bitmap = BitmapFactory.decodeStream(is);
	        is.close();
        } catch (Exception e) {
        }

        return bitmap;
	}
	
	
	/**
	* 向指定URL发送GET方法的请求
	* @param url 发送请求的URL
	* @param param 请求参数
	* List<NameValuePair> params=new ArrayList<NameValuePair>(); 
	* params.add(new BasicNameValuePair("key","value")
	* @return URL所代表远程资源的响应
	*/
	public static String sendPost(String url , List<NameValuePair> params){
		String result = "";
		HttpPost httpRequest=null; 
	    HttpResponse httpResponse; 
	    httpRequest=new HttpPost(url); 
	    try { 
            //发出HTTP request 
            httpRequest.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8)); 
            //取得HTTP response 
            httpResponse=new DefaultHttpClient().execute(httpRequest); 
            //若状态码为200 
            if(httpResponse.getStatusLine().getStatusCode()==200){ 
                //取出回应字串 
                String strResult=EntityUtils.toString(httpResponse.getEntity()); 
                result=strResult; 
            }else{ 
            	result="Error Response"+httpResponse.getStatusLine().toString(); 
            } 
        } catch (Exception e) { 
           
        }
		return result;		
	}
	
	
	/**
	* 向指定URL发送POST方法的请求
	* @param url 发送请求的URL
	* @param param 请求参数，请求参数应该是name1=value1&name2=value2的形式。
	* @return URL所代表远程资源的响应
	*/
	public static String sendPost(String url,String param){
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try{
			URL realUrl = new URL(url);
			//打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			//设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			//发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			//发送请求参数
			out.print(param);
			//flush 输出流的缓冲
			out.flush();
			//定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine())!= null){
				result += line;
			}
		}catch(Exception e){
			//System.out.println("发送POST 请求出现异常！" + e);
			e.printStackTrace();
		}finally{
			try{
				if (out != null){
					out.close();
				}
				if (in != null){
					in.close();
				}
			}catch (IOException ex){
				ex.printStackTrace();
			}
		}
		return result;
		
	}
	
	
	/**
	* 向指定URL发送GET方法的请求
	* @param url 发送请求的URL
	* @param param 请求参数，请求参数应该是name1=value1&name2=value2的形式。
	* @return URL所代表远程资源的响应
	*/
	public static String sendGet(String url , String param){
		String result = "";
		BufferedReader in = null;
		try{
			String urlName = url + "?" + param;
			URL realUrl = new URL(urlName);
			//打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			//设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			//建立实际的连接
			conn.connect();
			//获取所有响应头字段
			//Map<String, List<String>> map = conn.getHeaderFields();
			//遍历所有的响应头字段
			//for (String key : map.keySet()){
				//System.out.println(key + "--->" + map.get(key));
			//}
			//定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine())!= null){
				result +=line;
			}
		}catch(Exception e){
		 //System.out.println("发送GET请求出现异常！" + e);
		 e.printStackTrace();
		}finally{
			try{
				if (in != null){
					in.close();
				}
			}catch (IOException ex){
				ex.printStackTrace();
			}
		}
		return result;		
	}

}
