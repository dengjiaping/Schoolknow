package com.pw.schoolknow.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonUtil {
	
	/**
	 * 解析json
	 * @param jsonStr  json字符串
	 * @param params   json键码
	 * @param values   解析后添加参数
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String,Object>>  parseJson(String jsonStr,String[] params,String[] values){
		List<Map<String,Object>> all= new ArrayList<Map<String,Object>>();
		try{
			JSONArray jsonarr=new JSONArray(jsonStr);
			for(int i=0;i<jsonarr.length();i++){
				Map<String,Object> map=new HashMap<String, Object>();
				JSONObject jsonobj=jsonarr.getJSONObject(i);
				for(int j=0;j<params.length;j++){
					map.put(params[j],values[j]+jsonobj.getString(params[j]));
				}
				all.add(map);
			}
			
		}catch(Exception e){
			
		}
		return all;
		
	}
}
