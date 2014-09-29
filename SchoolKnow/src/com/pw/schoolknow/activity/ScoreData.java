package com.pw.schoolknow.activity;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pw.schoolknow.R;
import com.pw.schoolknow.adapter.ScoreAdapter;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.config.ServerConfig;
import com.pw.schoolknow.helper.JsonHelper;
import com.pw.schoolknow.helper.ScoreHelper;
import com.pw.schoolknow.utils.GetUtil;
public class ScoreData extends BaseActivity {
	
	private ListView listview;
	private List<Map<String,Object>> list;
	
	private float allScore=0;
	private float allCredit=0;
	private float Score=0;
	
	String stuName,studId,stuScore;
	
	private TextView rank;
	private TextView majorRank;
	private ProgressBar pb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_score_data);
		setTitle("成绩查询");
		setTitleBar(R.drawable.btn_titlebar_back,0);
		
		listview=(ListView) super.findViewById(R.id.score_data_listview);
		String json=getIntent().getStringExtra("jsondata");
		
		try {
			JSONObject JsonData = new JSONObject(json);
			stuName=JsonData.getString("name").toString();
			studId=JsonData.getString("stuid").toString();
			stuScore=JsonData.getString("score").toString();
			
			list=new JsonHelper(stuScore).parseJson(
					new String[]{"subject","score","extra_1","extra_2","demand","credit"},
					new String[]{"","","","","",""});
			
			for(Map<String,Object> map:list){
				if(map.get("demand").toString().trim().equals("必修课")&&
						!map.get("score").toString().trim().equals("合格")){
					float credit=Float.parseFloat(map.get("credit").toString());
					allScore+=ScoreHelper.getScore(map.get("score").toString())*credit;
					allCredit+=credit;
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		Score=(float)(Math.round(allScore/allCredit*10))/10;
		
		View lauout=LayoutInflater.from(this)
		          .inflate(R.layout.part_score_data_head, null, false);
		
		TextView tvName=(TextView) lauout.findViewById(R.id.score_data_head_name);
		tvName.setText("姓名:"+stuName);
		
		TextView tvStuid=(TextView) lauout.findViewById(R.id.score_data_head_stuid);
		tvStuid.setText("学号:"+studId);
		
		TextView aveScore=(TextView) lauout.findViewById(R.id.score_data_head_avescore);
		aveScore.setText("均学分绩:"+Score);
		
		rank=(TextView) lauout.findViewById(R.id.score_data_head_classrank);
		majorRank=(TextView) lauout.findViewById(R.id.score_data_head_majorank);
		pb=(ProgressBar) lauout.findViewById(R.id.score_data_head_pb);
		
		listview.addHeaderView(lauout);
		listview.setAdapter(new ScoreAdapter(list, ScoreData.this));
		
		new AsyncLoadRank().execute(studId);
		
	}
	
	//获取班级排名
	class AsyncLoadRank extends AsyncTask<String, Void, String>{
		protected String doInBackground(String... params) {
			String result=GetUtil.getRes(ServerConfig.HOST+"/schoolknow/api/getRank.php?stuid="+params[0]);
			return result;
		}

		protected void onPostExecute(String result) {
			String[] param=result.split("\\|");
			rank.setText(param[1]);
			majorRank.setText("专业排名:"+param[0]);
			super.onPostExecute(result);
			pb.setVisibility(View.GONE);
		}

	}

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		switch(buttonId){
		case 1:
			this.finish();
			break;
		case 2:
			break;
		case 3:
			break;
		default:
			break;
		}
		
	}

}
