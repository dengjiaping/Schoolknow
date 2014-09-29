package com.pw.schoolknow.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pw.schoolknow.config.ServerConfig;

public class RegUtils {
	
	/**
	 * ´Ó×Ö·û´®ÖÐ»ñÈ¡Í¼Æ¬µØÖ·
	 * @param s
	 * @return
	 */
	public static List<String> getImgFromString(String s)   
	{   
	   String regex;   
	   List<String> list = new ArrayList<String>();   
	   regex = "src=[\"|\'](.*?)[\"|\']";  
	   //regex = "<[img|IMG].*?src=[\'|\"](.*?)[\'|\"].*?/>";
	   Pattern pa = Pattern.compile(regex, Pattern.DOTALL);   
	   Matcher ma = pa.matcher(s);   
	   while (ma.find())   
	   {
		   String temp=ma.group().trim();
		   temp=temp.substring(5, temp.length()-1);
		   list.add(temp);   
	   }   
	   return list;   
	}
	
	public static List<String> getImgFromString(String s,String uid)   
	{   
	   String regex;   
	   List<String> list = new ArrayList<String>();   
	   regex = "src=[\"|\'](.*?)[\"|\']";  
	   //regex = "<[img|IMG].*?src=[\'|\"](.*?)[\'|\"].*?/>";
	   Pattern pa = Pattern.compile(regex, Pattern.DOTALL);   
	   Matcher ma = pa.matcher(s);   
	   while (ma.find())   
	   {
		   String temp=ma.group().trim();
		   temp=temp.substring(5, temp.length()-1);
		   String url=ServerConfig.SquarePath+"img/"+uid+"/"+temp;
		   list.add(url);   
	   }   
	   return list;   
	}
	
	/**
	 * °Ñ×Ö·û´®ÖÐÍ¼Æ¬Ìæ»»Îª¿Õ
	 * @param s
	 * @return
	 */
	public static String replaceImage(String s){
		return s.replaceAll("<[img|IMG].*?src=[\'|\"](.*?)[\'|\"].*?/>", "");
	}
	
	
	public static List<String> getMarketImgFromString(String s,String uid)   
	{   
	   String regex;   
	   List<String> list = new ArrayList<String>();   
	   regex = "src=[\"|\'](.*?)[\"|\']";  
	   //regex = "<[img|IMG].*?src=[\'|\"](.*?)[\'|\"].*?/>";
	   Pattern pa = Pattern.compile(regex, Pattern.DOTALL);   
	   Matcher ma = pa.matcher(s);   
	   while (ma.find())   
	   {
		   String temp=ma.group().trim();
		   temp=temp.substring(5, temp.length()-1);
		   String url=ServerConfig.HOST+"/schoolknow/plugin/market/img/"+uid+"/"+temp;
		   list.add(url);   
	   }   
	   return list;   
	}

}
