package com.pw.schoolknow.config;

public class ServerConfig {
	
	public static final String HOST="http://wangjian.ecjtu.org:80";
	//public static final String HOST="http://192.168.1.102:8082";
	
	public static final String SquarePath=HOST+"/schoolknow/Square/";
	
	//是否使开发者模式
	public final static boolean DEVELOP_MODE=true;
	
	//更新好友资料的时间间隔
	public final static int UPDATE_USER_INFO_TIME=7;

}
