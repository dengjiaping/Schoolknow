package com.pw.schoolknow.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.pw.schoolknow.R;
import com.pw.schoolknow.adapter.AskRankingAdapter;

public class AskRanking extends Activity {
	
	public String[][] data={
			{"舞影凌风","6"},
			{"舞影凌风","6"},
			{"舞影凌风","6"},
			{"舞影凌风","6"},
			{"舞影凌风","6"},
			{"舞影凌风","6"},
			{"舞影凌风","6"},
			{"舞影凌风","6"},
			{"舞影凌风","6"},
			{"舞影凌风","6"}
	};
	
	private ListView allLv;
	private ListView monthLv;
	private List<Map<String,Object>> allList;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_ask_ranking);
		
		allLv=(ListView) super.findViewById(R.id.ask_ranking_all);
		allList=new ArrayList<Map<String,Object>>();
		for(int i=0;i<data.length;i++){
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("rank",String.valueOf(i+1));
			map.put("name",data[i][0]);
			map.put("score",data[i][1]);
			allList.add(map);
		}
		
		monthLv=(ListView) super.findViewById(R.id.ask_ranking_month);
		
		AskRankingAdapter adapter=new AskRankingAdapter(allList, this);
		allLv.setAdapter(adapter);
		monthLv.setAdapter(adapter);
		
		//解决ScrollView嵌套listview冲突
		int totalHeight = 0;  
        for (int i = 0; i < adapter.getCount(); i++) {  
            View listItem = adapter.getView(i, null, allLv);  
            listItem.measure(0, 0);  
            totalHeight += listItem.getMeasuredHeight();  
        }  
        ViewGroup.LayoutParams params = allLv.getLayoutParams();  
        params.height = totalHeight + (allLv.getDividerHeight() * (adapter.getCount() - 1));  
        allLv.setLayoutParams(params); 
        monthLv.setLayoutParams(params);
		
	}
}
