package com.pw.schoolknow.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lidroid.xutils.BitmapUtils;
import com.pw.schoolknow.R;
import com.pw.schoolknow.activity.FriendZone;
import com.pw.schoolknow.activity.Index;
import com.pw.schoolknow.activity.PhotoView;
import com.pw.schoolknow.activity.SchoolFellowContent;
import com.pw.schoolknow.base.SchoolFellowBase;
import com.pw.schoolknow.helper.BitmapHelper;
import com.pw.schoolknow.utils.RegUtils;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.utils.TimeUtil;
import com.pw.schoolknow.utils.ViewHolder;
import com.pw.schoolknow.widgets.EmotionBox;
import com.pw.schoolknow.widgets.NoScrollGridView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.BufferType;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SchoolFellowAdapter extends BaseAdapter {
	
	private List<SchoolFellowBase> list;
	private Context context;
	public ListView lv;
	
	public Boolean enClick=true;
	public BitmapUtils headBm;
	
	public SchoolFellowAdapter(Context context,List<SchoolFellowBase> list){
		this.list=list;
		this.context=context;
		headBm=BitmapHelper.getHeadBitMap(context);
	}

	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void update(){
		notifyDataSetChanged();
	}
	
	public View getView(int position, View convertView, ViewGroup parent,Boolean b){
		enClick=b;
		return getView(position,convertView,parent);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
	        convertView = LayoutInflater.from(context)
	          .inflate(R.layout.item_school_fellow_lv, parent, false);
	    }
		 ImageView head = ViewHolder.get(convertView, R.id.schoolfellow_item_user_img);
		 TextView nick=ViewHolder.get(convertView, R.id.schoolfellow_item_user_nick);
		 TextView time=ViewHolder.get(convertView, R.id.schoolfellow_item_time);
		 TextView content=ViewHolder.get(convertView, R.id.schoolfellow_item_content);
		 NoScrollGridView gv=ViewHolder.get(convertView, R.id.schoolfellow_item_content_gv);
		 
		 TextView commentBtn=ViewHolder.get(convertView, R.id.schoolfellow_item_bar_comment);
		 TextView reBtn=ViewHolder.get(convertView, R.id.schoolfellow_item_bar_re);
		 TextView praiseBtn=ViewHolder.get(convertView, R.id.schoolfellow_item_bar_praise);
		 
		 SchoolFellowBase item=list.get(position);
		 
		 if(enClick){
			 content.setOnClickListener(new onBarClickListener(position,0));
			 commentBtn.setOnClickListener(new onBarClickListener(position,0));
		 }
		 reBtn.setOnClickListener(new onBarClickListener(position,1));
		 praiseBtn.setOnClickListener(new onBarClickListener(position,2,praiseBtn,item));
		 Drawable drawable=null;
		 if(item.isHasLike()){
			 drawable = context.getResources().getDrawable(R.drawable.timeline_icon_like);
		 }else{
			 drawable = context.getResources().getDrawable(R.drawable.timeline_icon_like_disable);
		 }
		 drawable.setBounds(0, 0, 32, 32);
	     praiseBtn.setCompoundDrawables(drawable, null, null, null);
		
		 nick.setText(item.getNickname());
		 time.setText(TimeUtil.getSquareTime(Long.parseLong(item.getTime())));
		 commentBtn.setText("评论("+item.getCommentNum()+")");

		 BitmapHelper.setHead(headBm, head, item.getUid());
		 final String userUid=item.getUid();
		 head.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent it=new Intent(context,FriendZone.class);
				it.putExtra("uid",userUid);
				context.startActivity(it);
			}
		});
		 
		 //获取发布内容中的图片地址
		 final List<String> imgList=RegUtils.getImgFromString(item.getContent(),item.getUid());
		 
		//九宫格图片显示
		 if(imgList.size() == 0){
			 gv.setVisibility(View.GONE);
		 }else{
			 gv.setVisibility(View.VISIBLE);
			 SFImgShowAdapter adapter=null;
			 gv.setNumColumns(4);
			 adapter=new SFImgShowAdapter(context, imgList,gv,R.layout.item_img_gv);
			 gv.setAdapter(adapter);
		 }
		
		 
		 //浏览图片
		 gv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				//Intent it=new Intent(context,ImgPreview.class);
				//it.putExtra("imgsrc",imgList.get(position));
				//context.startActivity(it);
				Intent it=new Intent(context,PhotoView.class);
				it.putStringArrayListExtra("data",(ArrayList<String>) imgList);
				it.putExtra("position", position);
				context.startActivity(it);
			}
		});
		//content.setText(Html.fromHtml(RegUtils.replaceImage(item.getContent()),
		//			null,null));
		content.setText(EmotionBox.convertNormalStringToSpannableString(context,
				 RegUtils.replaceImage(item.getContent())),BufferType.SPANNABLE);
		//content.setMovementMethod(LinkMovementMethod.getInstance());
		return convertView;
	}

	
	
	//心情工具条按钮事件
	class onBarClickListener implements OnClickListener{
		public int position;
		public int tag;
		public TextView like;
		public SchoolFellowBase item;
		
		public onBarClickListener(int position,int tag){
			this.position=position;
			this.tag=tag;
			like=null;
			this.item=null;
		}
		public onBarClickListener(int position,int tag,TextView like){
			this.position=position;
			this.tag=tag;
			this.like=like;
			this.item=null;
		}
		public onBarClickListener(int position,int tag,TextView like,SchoolFellowBase item){
			this.position=position;
			this.tag=tag;
			this.like=like;
			this.item=item;
		}
		public void onClick(View v) {
			switch(tag){
			case 0:
				SchoolFellowBase item=list.get(position);
				Intent it=new Intent(context,SchoolFellowContent.class);
				Bundle mBundle = new Bundle();  
			    mBundle.putSerializable(Index.SER_KEY,item);
			    mBundle.putInt("position", position);
			    it.putExtras(mBundle); 
				context.startActivity(it);
				break;
			case 1:
				T.showShort(context,"转发功能将在以后版本推出");
				break;
			case 2:
				if(like!=null&&this.item!=null){
					try{
						if(this.item.isHasLike()){
							T.showShort(context,"您已经赞过了~");
						}else{
							String val=like.getText().toString();
							 Pattern p=Pattern.compile("(\\d+)");
							 Matcher m=p.matcher(val);       
							 if(m.find()){
								 int l=Integer.parseInt(m.group(1));
							     like.setText("赞("+(++l)+")");
							     Drawable drawable = context.getResources().getDrawable(R.drawable.timeline_icon_like);
							     drawable.setBounds(0, 0, 32, 32);
							     like.setCompoundDrawables(drawable, null, null, null);
							     this.item.setHasLike(true);
							 } 
						}
						
					}catch(Exception e){
						
					}
				}
				break;
			default:
				break;
			}
		}
		
	}
	

}
