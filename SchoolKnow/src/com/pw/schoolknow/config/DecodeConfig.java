package com.pw.schoolknow.config;

import com.pw.schoolknow.utils.Sha1Util;

public class DecodeConfig {
	
	//头像名称
	public static String decodeHeadImg(String uid){
		return new Sha1Util().getDigestOfString(("p"+uid+"w").getBytes());
	}
	
	//获取头像下载地址
	public static String getHeadUrlById(String uid){
		return ServerConfig.HOST+"/schoolknow/manage/head/"+DecodeConfig.decodeHeadImg(uid)+".pw";
	}

}
