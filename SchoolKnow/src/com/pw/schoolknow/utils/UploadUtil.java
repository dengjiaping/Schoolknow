package com.pw.schoolknow.utils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class UploadUtil {
	
	private static final int TIME_OUT = 10*1000; //超时时间 
	private static final String CHARSET = "utf-8"; //设置编码 

	/** 
	* android上传文件到服务器 
	* @param file 需要上传的文件 
	* @param RequestURL 请求的rul 
	* 返回响应的内容 
	*/

	public static String uploadFile(File file,String RequestURL) 

	{ 

	String result = null; 
	String BOUNDARY = UUID.randomUUID().toString(); //边界标识 随机生成 
	String PREFIX = "--" , LINE_END = "\r\n"; 
	String CONTENT_TYPE = "multipart/form-data"; //内容类型 

	try { 
	URL url = new URL(RequestURL); 

	HttpURLConnection conn = (HttpURLConnection) url.openConnection(); 
	conn.setReadTimeout(TIME_OUT); 
	conn.setConnectTimeout(TIME_OUT); 
	conn.setDoInput(true); //允许输入流 
	conn.setDoOutput(true); //允许输出流 
	conn.setUseCaches(false); //不允许使用缓存 
	conn.setRequestMethod("POST"); //请求方式 
	conn.setRequestProperty("Charset", CHARSET); //设置编码
	conn.setRequestProperty("connection", "keep-alive"); 
	conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY); 

	if(file!=null) 

	{ 
	/** 
	* 当文件不为空，把文件包装并且上传 
	*/

	DataOutputStream dos = new DataOutputStream( conn.getOutputStream()); 

	StringBuffer sb = new StringBuffer(); 
	sb.append(PREFIX); 
	sb.append(BOUNDARY); 
	sb.append(LINE_END); 

	/** 
	* name用来上传文件的标示符,表单中的name
	* filename是文件的名字
	*/

	sb.append("Content-Disposition: form-data; name=\"uploadedfile\"; filename=\""+file.getName()+"\""+LINE_END); 

	sb.append("Content-Type: application/octet-stream; charset="+CHARSET+LINE_END); 

	sb.append(LINE_END); 
	dos.write(sb.toString().getBytes()); 

	InputStream is = new FileInputStream(file); 

	byte[] bytes = new byte[1024]; 
	int len = 0; 

	while((len=is.read(bytes))!=-1) 

	{

	dos.write(bytes, 0, len); 

	} 
	is.close(); 
	dos.write(LINE_END.getBytes()); 

	byte[] end_data = (PREFIX+BOUNDARY+PREFIX+LINE_END).getBytes(); 

	dos.write(end_data); 
	dos.flush(); 
	/** 
	* 获取响应码 200=成功 
	* 当响应成功，获取响应的流 
	*/
	//int res = conn.getResponseCode();

	// if(res==200) 

	// { 

	InputStream input = conn.getInputStream(); 
	StringBuffer sb1= new StringBuffer(); 

	int ss ; 
	while((ss=input.read())!=-1) 
	{ 
	sb1.append((char)ss); 

	} 

	result = sb1.toString(); 

	} 

	} catch (MalformedURLException e) { 
	e.printStackTrace(); 
	} catch (IOException e) { 
	e.printStackTrace(); 

	} 
	return result; 

	} 
	
	
	
}
