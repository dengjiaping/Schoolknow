package com.pw.schoolknow.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.BufferType;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.pw.schoolknow.R;
import com.pw.schoolknow.adapter.Comment2Adapter;
import com.pw.schoolknow.adapter.SFImgShowAdapter;
import com.pw.schoolknow.app.MyApplication;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.base.InformBase;
import com.pw.schoolknow.base.SchoolFellowBase;
import com.pw.schoolknow.config.InformConfig;
import com.pw.schoolknow.config.ServerConfig;
import com.pw.schoolknow.helper.BitmapHelper;
import com.pw.schoolknow.helper.JsonHelper;
import com.pw.schoolknow.helper.LoginHelper;
import com.pw.schoolknow.helper.NetHelper;
import com.pw.schoolknow.push.AsyncSend;
import com.pw.schoolknow.utils.GetUtil;
import com.pw.schoolknow.utils.RegUtils;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.utils.TimeUtil;
import com.pw.schoolknow.utils.ViewHolder;
import com.pw.schoolknow.widgets.EmotionBox;
import com.pw.schoolknow.widgets.NoScrollGridView;
import com.pw.schoolknow.widgets.EmotionBox.EmotionBoxClickListener;
import com.pw.schoolknow.widgets.MyAlertMenu;
import com.pw.schoolknow.widgets.MyAlertMenu.MyDialogMenuInt;

@SuppressLint("HandlerLeak")
public class SchoolFellowContent extends BaseActivity {
	
	private List<Map<String,Object>>  list;
	private Comment2Adapter adapter;
	
	private ListView lv;
	private EmotionBox emotionBox;
	
	public SchoolFellowBase item;
	public int position;
	
	public LoginHelper lh;
	
	public Context mcontext;
	
	public String targetId;
	
	public MyAlertMenu mam;
	
	public String userid="";
	public String userUid="";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_schoolfellow_content);
		
		Intent getIntent=getIntent();
		item=(SchoolFellowBase) getIntent.getSerializableExtra(Index.SER_KEY);
		position=getIntent.getIntExtra("position", 0);
		
		setTitle(item.getNickname());
		setTitleBar(R.drawable.btn_titlebar_back,0);
		
		lh=new LoginHelper(this);
		mcontext=SchoolFellowContent.this;
		userUid=item.getUid();
		
		lv=(ListView) super.findViewById(R.id.schoolfellow_content_comment_lv);
		
		list=new ArrayList<Map<String,Object>>();
		
		//View head=SchoolFellowSquare.adapter.getView(position,null,null,false);
		View head=SchoolFellowContent.getView(mcontext, item);
		lv.addHeaderView(head);
		
		
		adapter=new Comment2Adapter(this, list);
		lv.setAdapter(adapter);
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(final AdapterView<?> parent, View arg1, int position,
					long arg3) {
				@SuppressWarnings("unchecked")
				Map<String,Object> map=(Map<String, Object>) parent.getItemAtPosition(position);
				targetId=map.get("id").toString().trim();
				final String nick=map.get("nick").toString();
				final String copyVal=map.get("content").toString();
				final String targetUid=map.get("uid").toString();
				mam=new MyAlertMenu(mcontext, new String[]{"回复","复制","点赞","举报内容"});
				mam.setOnItemClickListener(new MyDialogMenuInt() {
					@SuppressLint("NewApi")
					public void onItemClick(int position) {
						switch(position){
						case 0:
							new AsyncLoadUserid().execute(targetUid);
							userUid=targetUid;
							emotionBox.SetValue("");
							emotionBox.setEditHint("回复:@"+nick);
							break;
						case 1:
							// 得到剪贴板管理器
							ClipboardManager cmb = (ClipboardManager) mcontext
							.getSystemService(Context.CLIPBOARD_SERVICE);
							String contentVal=copyVal.trim();
							int start=contentVal.indexOf("</at>");
							if(start!=-1){
								start+=5;
								contentVal=contentVal.substring(start);
							}
							cmb.setText(contentVal);
							T.showShort(mcontext,"已经复制到粘贴板");
							break;
						case 2:
							T.showShort(mcontext,"点赞成功,你猜对方知道不?");
							break;
						case 3:
							T.showShort(mcontext,"爱ta就疯狂的举报ta吧,反正没用");
							break;
						}
					}
				});
				
			}
		});
		
		
		emotionBox=new EmotionBox(this);
		emotionBox.setEditHint("我要吐槽~");
		emotionBox.setOnClick(new EmotionBoxClickListener() {
			public void onClick(String EditTextVal, View view) {
				final String input=EditTextVal;
				if(!lh.hasLogin()){
					T.showShort(mcontext, "请登录后再发布评论");
				}else if(EditTextVal.trim().length()==0){
					T.showShort(mcontext, "发布的内容不能为空");
				}else if(EditTextVal.length()>255){
					T.showShort(mcontext, "发布的内容不能超过256个字符");
				}else if(!NetHelper.isNetConnected(mcontext)){
					T.showShort(mcontext,R.string.net_error_tip);
					return;
				}else{
					emotionBox.setClickable(false);
					view.setClickable(false);
					new Thread(new Runnable() {
						public void run() {
							List<NameValuePair> params=new ArrayList<NameValuePair>(); 
							params.add(new BasicNameValuePair("uid",lh.getUid()));
							params.add(new BasicNameValuePair("token",lh.getToken()));
							params.add(new BasicNameValuePair("sfid",String.valueOf(item.getId())));
							params.add(new BasicNameValuePair("content",input.trim()));
							params.add(new BasicNameValuePair("target",targetId));
							String str=GetUtil.sendPost(ServerConfig.HOST+"/schoolknow/Square/insertNew.php", params);
							Message msg=new Message();
							msg.what=102;
							msg.obj=str;
							handler.sendMessage(msg);
						}
					}).start();
					
					//推送消息
					//if(userid.trim().length()!=0){
					if(userid.trim().length()!=0&&!userUid.equals(lh.getUid())){
						InformBase formItem=new InformBase(
								InformConfig.PUSH_SFComment,lh.getUid(), lh.getNickname(),"",
								TimeUtil.getCurrentTime()+"",String.valueOf(item.getId()),userUid);
						new AsyncSend(MyApplication.getInstance().getGson().toJson(formItem), userid.trim()).send();
					}
				}
			}
		});
		//启动线程获取评论
		new AsyncLoadComment().execute();
		//启动线程获取推送userid
		new AsyncLoadUserid().execute(item.getUid());
	};
	
	
	
	
	
	Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String res=msg.obj.toString().trim();
			if(msg.what==102){
				if(res.equals("success")){
					T.showShort(mcontext,"发布成功");
					emotionBox.SetValue("");
					emotionBox.setClickable(true);
					//更新界面
					list.clear();
					new AsyncLoadComment().execute();
				}else{
					T.showShort(mcontext,"发布失败,请重新尝试");
				}
			}
		}
		
	};
	
	
	//异步加载评论
	class AsyncLoadComment extends AsyncTask<String, Void, String>{
		protected String doInBackground(String... params) {
			String data=GetUtil.getRes(ServerConfig.HOST+"/schoolknow/Square/getComment.php?sfid="+item.getId());
			return data;
		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			try {
				List<Map<String,Object>> JsonData=new JsonHelper(result).parseJson(
						new String[]{"id","uid","nick","content","time"});
				list.addAll(JsonData);
				adapter.notifyDataSetInvalidated();
		       
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * 异步获取推送userid
	 * @author wei8888go
	 *
	 */
	class AsyncLoadUserid extends AsyncTask<String, Void, String>{
		protected String doInBackground(String... param) {
			String str=GetUtil.getRes(ServerConfig.HOST+"/schoolknow/api/getuserid.php?uid="+param[0]);
			return str;
		}
		protected void onPostExecute(String result) {
			userid=result;
			super.onPostExecute(result);
		}
		
		
	}
	
	public static View getView(final Context context,SchoolFellowBase item){
		final View convertView = LayoutInflater.from(context)
		          .inflate(R.layout.item_school_fellow_lv_2, null, false);
		ImageView head = ViewHolder.get(convertView, R.id.schoolfellow_item2_user_img);
		TextView nick=ViewHolder.get(convertView, R.id.schoolfellow_item2_user_nick);
		TextView time=ViewHolder.get(convertView, R.id.schoolfellow_item2_time);
		TextView content=ViewHolder.get(convertView, R.id.schoolfellow_item2_content);
		NoScrollGridView gv=ViewHolder.get(convertView, R.id.schoolfellow_item2_content_gv);
		TextView commentTip=ViewHolder.get(convertView, R.id.schoolfellow_item2_comment_tip);
		 
		commentTip.setText("评论( "+item.getCommentNum()+" )");
		nick.setText(item.getNickname());
		time.setText(TimeUtil.getCommentTime(Long.parseLong(item.getTime())));
		
		 BitmapUtils bitmapUtil=BitmapHelper.getHeadBitMap(context);
		 BitmapHelper.setHead(bitmapUtil, head, item.getUid());
		 
		//获取发布内容中的图片地址
		 final List<String> imgList=RegUtils.getImgFromString(item.getContent(),item.getUid());
		 
		//九宫格图片显示
		 if(imgList.size() == 0){
			 gv.setVisibility(View.GONE);
		 }else{
			 gv.setVisibility(View.VISIBLE);
			 SFImgShowAdapter adapter=null;
			 gv.setNumColumns(3);
			 adapter=new SFImgShowAdapter(context, imgList,gv,R.layout.item_img_gv);
			 gv.setAdapter(adapter);
		 }
		
		 
		 //浏览图片
		 gv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent it=new Intent(context,ImgPreview.class);
				it.putExtra("imgsrc",imgList.get(position));
				context.startActivity(it);
			}
		});
		content.setText(EmotionBox.convertNormalStringToSpannableString(context,
				 RegUtils.replaceImage(item.getContent())),BufferType.SPANNABLE);
		return convertView;
	}
	

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		switch(buttonId){
		case 1:
			finish();
			break;
		case 2:
			break;
		default:
			break;
		}
	}
	
}
