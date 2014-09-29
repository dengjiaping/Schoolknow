package com.pw.schoolknow.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonHelper {
	private  String jsonStr=null;
	
	public JsonHelper(String jsonStr){
		this.jsonStr=jsonStr;
	}
	
	public List<Map<String,Object>>  parseJson(String[] params,String[] values) throws Exception{
		List<Map<String,Object>> all= new ArrayList<Map<String,Object>>();
		JSONArray jsonarr=new JSONArray(jsonStr);
		for(int i=0;i<jsonarr.length();i++){
			Map<String,Object> map=new HashMap<String, Object>();
			JSONObject jsonobj=jsonarr.getJSONObject(i);
			for(int j=0;j<params.length;j++){
				map.put(params[j],values[j]+jsonobj.getString(params[j]));
			}
			all.add(map);
		}
		return all;
	}
	
	public List<Map<String,Object>>  parseJson(String[] params) throws Exception{
		List<Map<String,Object>> all= new ArrayList<Map<String,Object>>();
		JSONArray jsonarr=new JSONArray(jsonStr);
		for(int i=0;i<jsonarr.length();i++){
			Map<String,Object> map=new HashMap<String, Object>();
			JSONObject jsonobj=jsonarr.getJSONObject(i);
			for(int j=0;j<params.length;j++){
				map.put(params[j],jsonobj.getString(params[j]));
			}
			all.add(map);
		}
		return all;
	}

}
