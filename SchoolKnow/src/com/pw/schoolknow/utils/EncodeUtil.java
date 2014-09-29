package com.pw.schoolknow.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class EncodeUtil {
	
	
	/**
	 * ×ª»¯Îªutf-8
	 * @param str
	 * @return
	 */
	public static String ToUtf8(String str){
		try {
			return URLEncoder.encode(str,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;		
	}

}
