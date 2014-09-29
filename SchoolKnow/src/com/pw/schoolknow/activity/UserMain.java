package com.pw.schoolknow.activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pw.schoolknow.R;
import com.pw.schoolknow.base.BaseGroupActivity;
import com.pw.schoolknow.widgets.MyScrollLayout;
import com.pw.schoolknow.widgets.MyScrollLayout.OnViewChangeListener;

@SuppressWarnings("deprecation")
public class UserMain extends BaseGroupActivity implements OnViewChangeListener{
	
	@ViewInject(R.id.ScrollLayout)
	private MyScrollLayout mScrollLayout;
	private FrameLayout[] frameLauout;

	
	public TextView[] titleTv;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_main_act);
		
		LinearLayout titleBar=(LinearLayout) LayoutInflater.from(this)
		          .inflate(R.layout.user_main_titlebar,null);
		int tvCount=titleBar.getChildCount();
		titleTv=new TextView[tvCount];
		for(int i=0;i<tvCount;++i){
			titleTv[i]=(TextView) titleBar.getChildAt(i);
			titleTv[i].setOnClickListener(new OnClickImpl());
		}
		setCurPoint(0);
		setTitle(titleBar);
		
		ViewUtils.inject(this);
		int count=mScrollLayout.getChildCount();
		frameLauout=new FrameLayout[count];
		for(int i=0;i<count;++i){
			frameLauout[i]=(FrameLayout) mScrollLayout.getChildAt(i);
			switchActivity(i);
		}
		mScrollLayout.SetOnViewChangeListener(this);
	}
	
	public class OnClickImpl implements android.view.View.OnClickListener{
		public void onClick(View v) {
			for(int i=0;i<titleTv.length;++i){
				if(titleTv[i]==v){
					mScrollLayout.snapToScreen(i);
					setCurPoint(i);
					break;
				}
			}
		}
		
	}
	
	public void setCurPoint(int index){
		for(TextView tv:titleTv){
			tv.setTextColor(Color.parseColor("#484445"));
		}
		titleTv[index].setTextColor(Color.parseColor("#0079FF"));
	}
	
	private void switchActivity(int id){
		Intent it=null;
		switch(id){
		case 0:
			it=new Intent(this,FriendsList.class);
			break;
		case 1:
			it=new Intent(this,User.class);
			break;
		case 2:
			it=new Intent(this,Inform.class);
			break;
		default:
			break;
		}
		it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);		// 增加标记 
		Window subActivity = this.getLocalActivityManager().startActivity(
				"subActivity", it);
		frameLauout[id].removeAllViews();
		frameLauout[id].addView(subActivity.getDecorView(),
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	}


	@Override
	public void OnViewChange(int view) {
		setCurPoint(view);
	}

	protected void HandleTitleBarEvent(int buttonId, View v) {
		// TODO Auto-generated method stub
		
	}

}
