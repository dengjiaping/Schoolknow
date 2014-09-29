package com.pw.schoolknow.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternUtils {
	
	/**
	 * 判断邮箱
	 * @param str
	 * @return
	 */
	
	public static Boolean CheckEmail(String str){
	   String check = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
	   Pattern regex = Pattern.compile(check);
	   Matcher matcher = regex.matcher(str);
	   return  matcher.matches();
	}
	
	/**
	 * 判断密码是不是全部为数字或者字母
	 * @param str
	 * @return
	 */
	public static Boolean CheckPwd(String str){
		   String check = "^\\w+$";
		   Pattern regex = Pattern.compile(check);
		   Matcher matcher = regex.matcher(str);
		   return  matcher.matches();
	}
}
