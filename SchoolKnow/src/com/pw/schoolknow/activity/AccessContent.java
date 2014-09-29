package com.pw.schoolknow.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.pw.schoolknow.R;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.widgets.MyPj;

public class AccessContent extends BaseActivity {
	
	public String[] pj_lv_tip={
			"教风严谨，为人师表",
			"严格要求学生，批改作业，辅导答疑认真负责",
			"注意师生之间沟通，经常听取学生意见",
			"备课认真，讲课熟练",
			"授课层次分明，概念准确，内容充实，重点突出",
			"用普通话教学，语言清晰流畅，生动有趣",
			"不照本宣科，能理论联系实际",
			"合理有效的运用现代教学技术手段上课，板书工整",
			"因材施教，注重启发，鼓励学生发表自己的看法",
			"通过教学能掌握课程内容，提高了学习能力"
	};
	
	private LinearLayout pj_layout;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_access_content);
		setTitle("在线评教");
		setTitleBar(0,0);
		
		pj_layout=(LinearLayout) super.findViewById(R.id.access_content_selector);
		for(int i=0;i<pj_lv_tip.length;i++){
			MyPj pj=new MyPj(this,(i+1)+"、"+pj_lv_tip[i]);
			pj_layout.addView(pj);
		}
	}

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		// TODO Auto-generated method stub

	}

}
