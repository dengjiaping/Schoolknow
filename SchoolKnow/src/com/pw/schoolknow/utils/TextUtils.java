package com.pw.schoolknow.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wei8888go
 *
 */
public class TextUtils {
	
	
	/**
	 * 字符串是否全部是数字
	 * @param str
	 * @return
	 */
	public static boolean isAllNum(String str){
		Pattern pattern=Pattern.compile("[0-9]*");
		Matcher isNum=pattern.matcher(str);
		return isNum.matches();
	}
	
	/**
	 * 字符串是否为空
	 * @param str
	 * @return
	 */
	public static boolean isSpace(String str){
		String tempStr=str.trim();
		if(tempStr.length()==0&&tempStr.equals("")){
			return true;
		}
		return false;
	}
	
	/**
	 * str是否包含contain
	 * @param str
	 * @param contain
	 * @return
	 */
	public static boolean contain(String str,String contain){
		return str.indexOf(contain)!=-1;
	}
	
	

}
