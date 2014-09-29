package com.pw.schoolknow.helper;

public class CollegeHelper {
	
	public final static String[] collegeName={
		"土木建筑学院","机电工程学院","电气与电子工程学院","信息工程学院"
	      ,"软件学院","体育学院","基础科学学院","外国语学院","艺术学院","国防生学院"
	       ,"经济管理学院","人文社会科学学院","国际学院","轨道交通学院","继续教育学院"
	};
	
	public final static String[] collegeId={
		"01","03","02","06","21","05","08","09","11","61","04","07","12","13","00"
	};
	
	public static String getCollegeName(String Id){
		for(int i=0;i<collegeId.length;i++){
			if(collegeId[i].equals(Id)){
				return collegeName[i];
			}
		}
		return collegeName[0];
	}

}
