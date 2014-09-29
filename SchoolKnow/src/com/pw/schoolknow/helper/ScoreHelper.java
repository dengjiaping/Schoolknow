package com.pw.schoolknow.helper;

public class ScoreHelper {
	
	/**
	 * 计算分数
	 * @param str
	 * @return
	 */
	public static float getScore(String str){
		String score=str.trim();
		float myScore=0;
		try{
			myScore=Float.parseFloat(score);
		}catch(Exception e){
			if(score.equals("不及格")||score.equals("不合格")){
				myScore=45;
			}else if(score.equals("及格")||score.equals("合格")){
				myScore=60;
			}else if(score.equals("中等")){
				myScore=70;
			}else if(score.equals("良好")){
				myScore=80;
			}else if(score.equals("优秀")){
				myScore=90;
			}else if(score.equals("免修")){
				myScore=85;
			}
		}
		return myScore;
	}

}
