package com.pw.schoolknow.helper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TermHelper {
	
	/**
	 * 获取当前学年
	 * @return
	 */
	public static String getNowTerm(){
		String temp=null;
		int year=Calendar.getInstance().get(Calendar.YEAR);
		int month=Calendar.getInstance().get(Calendar.MONTH)+ 1;
		if(month>8){
			temp=year+"-"+(year+1)+"-1";
		}else if(month<2){
			temp=(year-1)+"-"+year+"-1";
		}else{
			temp=year+"-"+(year+1)+"-2";
		}
		return temp;
	}
	
	
	/**
	 * 获取当前5个学年
	 * @return
	 */
	public static List<CharSequence> getAllTerm(){
		List<CharSequence> list=new ArrayList<CharSequence>();
		int year=Calendar.getInstance().get(Calendar.YEAR);
		int month=Calendar.getInstance().get(Calendar.MONTH)+ 1;
		year=month>8?year:year-1;
		
		for(int i=year;i>year-4;i--){
			if(i==year){
				if(month>8){
					list.add(i+"-"+(i+1)+"上学期");
				}else{
					list.add(i+"-"+(i+1)+"上学期");
					list.add(i+"-"+(i+1)+"下学期");
				}
			}else{
				list.add(i+"-"+(i+1)+"上学期");
				list.add(i+"-"+(i+1)+"下学期");
			}
		}
		return list;
		
	}
	
	/**
	 * 把2012-2013上学期转化为2012.1
	 * @return
	 */
	public static String getNumTerm(String Term){
		String year=Term.substring(0,4);
		String term=Term.indexOf("上学期")!=-1?"1":"2";
		return year+"."+term;		
	}
	
	/**
	 * 将2012.1转化为2012-2013上学期
	 * @param Term
	 * @return
	 */
	public static String getStringTerm(String Term){
		int year=Integer.parseInt(Term.substring(0,4));
		String term=Term.substring(5,6).equals("1")?"上学期":"下学期";
		return year+"-"+(year+1)+term;
	}
	
	/**
	 * 将2012.1转化为2012-2013-1
	 * @param Term
	 * @return
	 */
	public static String getStringTerm2(String Term){
		int year=Integer.parseInt(Term.substring(0,4));
		String term=Term.substring(5,6).equals("1")?"1":"2";
		return year+"-"+(year+1)+"-"+term;
	}
	
	/**
	 * 将2012-2013-1转化为2012-2013上学期
	 * @param Term
	 * @return
	 */
	public static String getStringTerm3(String Term){
		int year=Integer.parseInt(Term.substring(0,4));
		String[] data=Term.split("\\-");
		String term=data[2].equals("1")?"上学期":"下学期";
		return year+"-"+(year+1)+term;
	}
	
	/**
	 * 将2012.1数组转化为2012-2013上学期数组
	 * @param term
	 * @return
	 */
	public static String[] getTermArr(String[] term){
		for(int i=0;i<term.length;i++){
			term[i]=getStringTerm(term[i]);
		}
		return term;
	}
	
	
	/**
	 * 获得当前的年级
	 * @return
	 */
	public static List<CharSequence> getGrade(){
		List<CharSequence> list=new ArrayList<CharSequence>();
		int year=Calendar.getInstance().get(Calendar.YEAR);
		int month=Calendar.getInstance().get(Calendar.MONTH)+ 1;
		int startYear=month>=8?year:year-1;
		for(int i=startYear; i>startYear-5; i--){
			list.add(String.valueOf(i));
		}
		return list;
		
	}
	
	
}

