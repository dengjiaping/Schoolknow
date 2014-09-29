package com.pw.schoolknow.utils;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间工具类
 * 
 * @author way
 * 
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtil {
	
	public static long getCurrentTime(){
		return System.currentTimeMillis();
	}

	public static String converTime(long time) {
		long currentSeconds = System.currentTimeMillis() / 1000;
		long timeGap = currentSeconds - time / 1000;// 与现在时间相差秒数
		String timeStr = null;
		if (timeGap > 3 * 24 * 60 * 60) {
			timeStr = getDayTime(time) + " " + getMinTime(time);
		} else if (timeGap > 24 * 2 * 60 * 60) {// 2天以上就返回标准时间
			timeStr = "前天 " + getMinTime(time);
		} else if (timeGap > 24 * 60 * 60) {// 1天-2天
			timeStr = timeGap / (24 * 60 * 60) + "昨天 " + getMinTime(time);
		} else if (timeGap > 60 * 60) {// 1小时-24小时
			timeStr = timeGap / (60 * 60) + "今天 " + getMinTime(time);
		} else if (timeGap > 60) {// 1分钟-59分钟
			timeStr = timeGap / 60 + "今天 " + getMinTime(time);
		} else {// 1秒钟-59秒钟
			timeStr = "今天 " + getMinTime(time);
		}
		return timeStr;
	}

	public static String getChatTime(long time) {
		long currentSeconds = System.currentTimeMillis();
		long timeGap = (currentSeconds - time )/1000;
		String timeStr = null;
		int lastDay=getDay(time);
		int nowDay=getDay(currentSeconds);
		
		int DayGap=Math.abs(nowDay-lastDay);
		if(nowDay-lastDay==0&&timeGap <24 * 60 * 60){
			timeStr =getHourAndMin(time);
		}else if(timeGap <2 * 24 * 60 * 60&&(DayGap==1||DayGap>=27)){
			timeStr = "昨天 "+ getHourAndMin(time);
		}else if(timeGap <3 * 24 * 60 * 60&&(DayGap==2||DayGap>=27)){
			timeStr = "前天 "+ getHourAndMin(time);
		}else{
			SimpleDateFormat format = new SimpleDateFormat("MM/dd HH:mm:ss");
			timeStr =format.format(new Date(time));
		}
		return timeStr;
	}
	
	public static String getMarketTime(long time){
		long currentSeconds = System.currentTimeMillis() / 1000;
		long timeGap = currentSeconds - time / 1000;// 与现在时间相差秒数
		String timeStr = null;
		if(timeGap<60){
			timeStr=timeGap+"秒前";
		}else if(timeGap<3600){
			timeStr=(int)(timeGap/60)+"分钟前";
		}else if(timeGap<60*60*5){
			timeStr=(int)(timeGap/3600)+"小时前";
		}else if(timeGap<60*60*24){
			timeStr="今天"+getHourAndMin(time);
		}else{
			timeStr=getDayTime(time);
		}
		return timeStr;
	}
	
	
	public static String getInformTime(long time){
		long currentSeconds = System.currentTimeMillis() / 1000;
		long timeGap = currentSeconds - time / 1000;// 与现在时间相差秒数
		String timeStr = null;
		if (timeGap > 3 * 24 * 60 * 60) {
			timeStr = getDayTime(time);
		}else if (timeGap > 24 * 2 * 60 * 60){
			timeStr = "前天 " + getHourAndMin(time);
		}else if (timeGap > 24 * 60 * 60) {// 1天-2天
			timeStr = "昨天 " + getHourAndMin(time);
		} else if (timeGap > 60 * 60) {// 1小时-24小时
			timeStr ="今天 "+getHourAndMin(time);
		}
		return timeStr;		
	}
	
	/**
	 * 评论专用时间
	 * @param time
	 * @return
	 */
	public static String getCommentTime(long time){
		long currentSeconds = System.currentTimeMillis();
		long timeGap = (currentSeconds - time )/1000;
		String timeStr = null;
		int lastDay=getDay(time);
		int nowDay=getDay(currentSeconds);
		
		int DayGap=Math.abs(nowDay-lastDay);
		if(nowDay-lastDay==0&&timeGap <24 * 60 * 60){
			timeStr ="今天" +getHourAndMin(time);
		}else if(timeGap <2 * 24 * 60 * 60&&(DayGap==1||DayGap>=27)){
			timeStr = "昨天 " + getHourAndMin(time);
		}else if(timeGap <3 * 24 * 60 * 60&&(DayGap==2||DayGap>=27)){
			timeStr = "前天 " + getHourAndMin(time);
		}else{
			timeStr =getDayTime(time);
		}
		return timeStr;
	}
	
	/**
	 * 动态专用时间
	 * @param time
	 * @return
	 */
	public static String getSquareTime(long time){
		long currentSeconds = System.currentTimeMillis();
		long timeGap = (currentSeconds - time )/1000;
		String timeStr = null;
		int lastDay=getDay(time);
		int nowDay=getDay(currentSeconds);
		
		int DayGap=Math.abs(nowDay-lastDay);
		if(nowDay-lastDay==0&&timeGap <24 * 60 * 60){
			timeStr ="今天" +getHourAndMin(time);
		}else if(timeGap <2 * 24 * 60 * 60&&(DayGap==1||DayGap>=27)){
			timeStr = "昨天 " + getHourAndMin(time);
		}else if(timeGap <3 * 24 * 60 * 60&&(DayGap==2||DayGap>=27)){
			timeStr = "前天 " + getHourAndMin(time);
		}else{
			timeStr =getMinTime(time);
		}
		return timeStr;
	}



	public static String getDayTime(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(new Date(time));
	}

	public static String getMinTime(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return format.format(new Date(time));
	}
	
	public static String getSecondTime(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date(time));
	}
	
	public static String getUpdateTime(long time) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		return format.format(new Date(time));
	}
	
	//2013年11月09日 09:00 周六
	public static String getYmdHmW(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
		Date date=new Date(time);
		return (format.format(date)+" "+weekday(getDayOfWeek(date)));
	}
	
	//2013/11/09 周六
	public static String getYmdW(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		Date date=new Date(time);
		return (format.format(date)+" "+weekday(getDayOfWeek(date)));
	}

	public static String getHourAndMin(long time) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		return format.format(new Date(time));
	}

	public static String getDayString(long timesamp) {
		String result = "";
		final Calendar calendarToday = Calendar.getInstance();
		calendarToday.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		calendarToday.setTimeInMillis(System.currentTimeMillis());

		final Calendar calendarday = Calendar.getInstance();
		calendarday.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		calendarday.setTimeInMillis(timesamp);

		@SuppressWarnings("static-access")
		int time = calendarToday.DAY_OF_MONTH - calendarday.DAY_OF_MONTH;

		if (time == 2) {
			result = "前天 ";
		} else if (time == 1) {
			result = "昨天 ";
		} else if (time == 0) {
			result = "今天 "+getHourAndMin(time);
		} else {
			result = time + "天前 ";
			
		}

		return result;
	}
	
	public static String getCommmentString(long timesamp) {
		String result = "";
		final Calendar calendarToday = Calendar.getInstance();
		calendarToday.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		calendarToday.setTimeInMillis(System.currentTimeMillis());

		final Calendar calendarday = Calendar.getInstance();
		calendarday.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		calendarday.setTimeInMillis(timesamp);

		@SuppressWarnings("static-access")
		int time = calendarToday.DAY_OF_MONTH - calendarday.DAY_OF_MONTH;

		if (time == 2) {
			result = "前天 "+getHourAndMin(timesamp);
		} else if (time == 1) {
			result = "昨天 "+getHourAndMin(timesamp);
		} else if (time == 0) {
			result = "今天 "+getHourAndMin(timesamp);
		} else {
			result = getMinTime(time);
		}

		return result;
	}
	
	/**
	 * 返回星期几
	 * @return
	 */
	public static int getDayOfWeek(long timesamp){
		 Calendar calendar = Calendar.getInstance();
	     Date date = new Date(timesamp);
	     calendar.setTime(date);
	     if(calendar.get(Calendar.DAY_OF_WEEK)==1){
	    	 return 7;
	     }else{
	    	 return calendar.get(Calendar.DAY_OF_WEEK)-1;
	     }	
	}
	
	public static int getDayOfWeek(Date date){
		 Calendar calendar = Calendar.getInstance();
	     calendar.setTime(date);
	     if(calendar.get(Calendar.DAY_OF_WEEK)==1){
	    	 return 7;
	     }else{
	    	 return calendar.get(Calendar.DAY_OF_WEEK)-1;
	     }	
	}
	
	/**
	 * 返回今天是星期几
	 * @return
	 */
	public static int getDayOfWeek(){
		 Calendar calendar = Calendar.getInstance();
	     Date date = new Date();
	     calendar.setTime(date);
	     if(calendar.get(Calendar.DAY_OF_WEEK)==1){
	    	 return 7;
	     }else{
	    	 return calendar.get(Calendar.DAY_OF_WEEK)-1;
	     }	
	}
	
	/**
	 *根据数字 获取星期文字
	 * @param week
	 * @return
	 */
	public static String weekday(int week){
		String weekday = null;
		switch(week){
		case 1:weekday="星期一";break;
		case 2:weekday="星期二";break;
		case 3:weekday="星期三";break;
		case 4:weekday="星期四";break;
		case 5:weekday="星期五";break;
		case 6:weekday="星期六";break;
		case 7:weekday="星期日";break;
		default:weekday="星期出错";
		}
		return weekday;
	}
	
	/**
	 * 2个时间戳相隔多少天
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static int betweenDays(long beginDate,long endDate){
		long timeGap = (endDate - beginDate )/1000;
		int dayGap=getDay(endDate)-getDay(beginDate);
		int day=24*60*60;
		if(timeGap<0){
			return -1;
		}else if(dayGap==0&&timeGap<day){
			return 0;
		}else if(timeGap<2*day){
			return 1;
		}else if(timeGap<3*day){
			return 2;
		}else{
			return (int)(timeGap/day);	
		}		
	}
	
	/**
	 * 新闻专用时间间隔(单位天)
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static int newBetweenDays(long beginDate,long endDate){
		long timeGap = (endDate - beginDate )/1000;
		int dayGap=getDay(endDate)-getDay(beginDate);
		int day=24*60*60;
		if(dayGap==0&&timeGap<day){
			return 0;
		}else if(timeGap<2*day){
			return 1;
		}else if(timeGap<3*day){
			return 2;
		}else{
			return (int)(timeGap/day);	
		}	
	}
	
	public static int betweenDays(long endDate){
		return betweenDays(System.currentTimeMillis(), endDate);
	}
	
	
	/**
	 * 生成对应的时间戳(毫微秒)
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @param minute
	 * @return
	 */
	public static long createtimesamp(int year,int month,int day,int hour,int minute){
		Calendar ca=Calendar.getInstance();
		ca.set(year, month-1, day,hour, minute);
		return ca.getTimeInMillis();		
	}
	
	
	public static int  getYear(long timesamp){
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		return Integer.parseInt(format.format(new Date(timesamp)));
	}
	
	public static int getMonth(long timesamp){
		SimpleDateFormat format = new SimpleDateFormat("MM");
		return Integer.parseInt(format.format(new Date(timesamp)));
	}
	
	public static int getDay(long timesamp){
		SimpleDateFormat format = new SimpleDateFormat("dd");
		return Integer.parseInt(format.format(new Date(timesamp)));
	}
	
	public static int getHour(long timesamp){
		SimpleDateFormat format = new SimpleDateFormat("HH");
		return Integer.parseInt(format.format(new Date(timesamp)));
	}
	
	public static int getMin(long timesamp){
		SimpleDateFormat format = new SimpleDateFormat("mm");
		return Integer.parseInt(format.format(new Date(timesamp)));
	}
}
