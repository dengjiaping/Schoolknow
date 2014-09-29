package com.pw.schoolknow.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.pw.schoolknow.R;
import com.pw.schoolknow.adapter.AskGvAdapter;
import com.pw.schoolknow.adapter.EmotionAdapter;
import com.pw.schoolknow.adapter.ViewPagerAdapter;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.config.EmotionConfig;
import com.pw.schoolknow.config.PathConfig;
import com.pw.schoolknow.config.ServerConfig;
import com.pw.schoolknow.helper.InputHelper;
import com.pw.schoolknow.helper.LoginHelper;
import com.pw.schoolknow.utils.FileUtils;
import com.pw.schoolknow.utils.PictureUtil;
import com.pw.schoolknow.utils.SelectPicUtil;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.utils.TimeUtil;
import com.pw.schoolknow.widgets.MyProgressBar;


@SuppressLint({ "HandlerLeak", "DefaultLocale" })
public class NewAsk  extends BaseActivity{
	
	private LinearLayout emotionBox;
	private boolean emotionBoxShow=false;
	private LinearLayout toolBarLayout;
	private EditText input;
	private GridView imgGv;
	private AskGvAdapter gvAdapter;
	private Bitmap myBitmap;   //图片Bitmap
	private String picAddr="";
	
	//默认表情
	private ViewPager df_vp=null;
	private LinearLayout df_dot_layout;
	private List<View> default_emotion_list=null;
	private int currentIndex;
	private ImageView[] dots ;
	private List<String> keys;
	
	//上传图片
	public List<Map<String,Object>> picList;
	private byte[] mContent;
	public String contentVal;
	public int k=0;
	public String picName;
	public List<String> imgList;
	public MyProgressBar waitLoadPic;
	
	public MyProgressBar mpb;
	public String imgPath="";
	private LoginHelper lh; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_ask_new_question);
		setTitleBar(R.drawable.btn_titlebar_back,"",0,"发布");
		
		lh=new LoginHelper(this);
		
		String param=getIntent().getStringExtra("param");
		if(param==null||param.equals("")){
			param="";
		}
		
		input=(EditText) super.findViewById(R.id.ask_new_question_content);
		input.setOnClickListener(new toolBarOnclickListener());
		imgGv=(GridView) super.findViewById(R.id.ask_new_question_gv_img);
		emotionBox=(LinearLayout) super.findViewById(R.id.ask_emotionbox);
		toolBarLayout=(LinearLayout) super.findViewById(R.id.ask_new_question_titlebar);
		for(int i=0;i<toolBarLayout.getChildCount();i++){
			ImageView img=(ImageView) toolBarLayout.getChildAt(i);
			img.setTag(i);
			img.setOnClickListener(new toolBarOnclickListener());
		}
		
		imgList=new ArrayList<String>();
		
		//载入默认表情
		initDefaultEmotion();
		Set<String> keySet =new EmotionConfig().getFaceMap()
				.keySet();
		keys = new ArrayList<String>();
		keys.addAll(keySet);
		
		picList=new ArrayList<Map<String,Object>>();
		gvAdapter=new AskGvAdapter(this, picList);
		imgGv.setAdapter(gvAdapter);
		imgGv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				picList.remove(position);
				gvAdapter.notifyDataSetChanged();
			}
		});
		
		if(param.equals("schoolfellow")){
			setTitle("发表动态");
			input.setHint("把好玩的事和校友们一起分享吧！");
		}
		
		
	}
	
	/**
	 * 默认表情初始化
	 */
	public void initDefaultEmotion(){
		df_vp=(ViewPager) super.findViewById(R.id.default_emotion_viewpager);
		df_dot_layout=(LinearLayout) super.findViewById(R.id.default_emotion__viewpager_dot);
		default_emotion_list = new ArrayList<View>();
		dots = new ImageView[EmotionConfig.NUM_PAGE];
		for (int i = 0; i < EmotionConfig.NUM_PAGE; ++i){
			default_emotion_list.add(getGridView(i));
			dots[i]=new ImageView(this);
			dots[i].setEnabled(false);
			dots[i].setImageResource(R.drawable.dot_2);
			LinearLayout.LayoutParams param=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			 param.setMargins(10, 0, 10,0);
			 df_dot_layout.addView(dots[i],param);
		}
		currentIndex = 0;
	    dots[currentIndex].setEnabled(true);
		df_vp.setAdapter(new ViewPagerAdapter(this,default_emotion_list));
		df_vp.setOnPageChangeListener(new setOnPageChangeListenerImp());
		
	}
	/**
	 * 默认表情分页
	 * @param i
	 * @return
	 */
	private GridView getGridView(int i) {
		GridView gv = new GridView(this);
		gv.setNumColumns(7);
		gv.setSelector(new ColorDrawable(Color.TRANSPARENT));// 屏蔽GridView默认点击效果
		gv.setBackgroundColor(Color.TRANSPARENT);
		gv.setCacheColorHint(Color.TRANSPARENT);
		gv.setHorizontalSpacing(7);
		gv.setVerticalSpacing(12);
		gv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		gv.setGravity(Gravity.CENTER);
		gv.setAdapter(new EmotionAdapter(this,i));
		gv.setOnTouchListener(forbidenScroll());
		gv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				 // 表情中的删除键
				if(position==EmotionConfig.NUM){
					int selection = input.getSelectionStart();
					String text = input.getText().toString();
					if (selection > 0) {
						String text2 = text.substring(selection - 1);
						if ("]".equals(text2)) {
							int start = text.lastIndexOf("[");
							int end = selection;
							input.getText().delete(start, end);
							return;
						}
						input.getText().delete(selection - 1, selection);
					}
				}else{
					//在EditText中显示表情
					int count = currentIndex * EmotionConfig.NUM + position;
					Bitmap bitmap = BitmapFactory.decodeResource(
							getResources(), (Integer) new EmotionConfig().getFaceMap().values()
									.toArray()[count]);
					if (bitmap != null) {
						int rawHeigh = bitmap.getHeight();
						int rawWidth = bitmap.getHeight();
						int newHeight = 40;
						int newWidth = 40;
						// 计算缩放因子
						float heightScale = ((float) newHeight) / rawHeigh;
						float widthScale = ((float) newWidth) / rawWidth;
						// 新建立矩阵
						Matrix matrix = new Matrix();
						matrix.postScale(heightScale, widthScale);
						// 设置图片的旋转角度
						// matrix.postRotate(-30);
						// 设置图片的倾斜
						// matrix.postSkew(0.1f, 0.1f);
						// 将图片大小压缩
						// 压缩后图片的宽和高以及kB大小均会变化
						Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0,
								rawWidth, rawHeigh, matrix, true);
						ImageSpan imageSpan = new ImageSpan(NewAsk.this,
								newBitmap);
						String emojiStr = keys.get(count);
						SpannableString spannableString = new SpannableString(
								emojiStr);
						spannableString.setSpan(imageSpan,
								emojiStr.indexOf('['),
								emojiStr.indexOf(']') + 1,
								Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						input.append(spannableString);
					}
				}
			}
		});
		return gv;
	}
	
	class setOnPageChangeListenerImp implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}

		@Override
		public void onPageSelected(int position) {
			setCurDot(position);
		}
		
	}
	
	//点切换
	private void setCurDot(int positon){
		 if (positon < 0 || positon > EmotionConfig.NUM_PAGE - 1 || currentIndex == positon) {
	            return;
	      }
		 dots[positon].setEnabled(true);
	     dots[currentIndex].setEnabled(false);
	     currentIndex = positon;
	}
	
	// 防止乱pageview乱滚动
 	private OnTouchListener forbidenScroll() {
 		return new OnTouchListener() {
 			@Override
			public boolean onTouch(View v, MotionEvent event) {
 				if (event.getAction() == MotionEvent.ACTION_MOVE) {
 					return true;
 				}
 				return false;
 			}
 		};
 	}
	
	
	class toolBarOnclickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			//隐藏表情框
			if(v==input){
				emotionBoxShow=false;
				emotionBox.setVisibility(View.GONE);
				return;
			}
			switch(Integer.parseInt(v.getTag().toString())){
			//选择相机
			case 0:
				picName=String.valueOf(TimeUtil.getCurrentTime())+".jpg";
				Intent intent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				FileUtils.createPath(PathConfig.SavePATH);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(PathConfig.SavePATH,picName)));
				startActivityForResult(intent, 0);
				break;
			case 1:
				//选择图库
				 //Intent it = new Intent(
                 //Intent.ACTION_PICK,
                 //android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				 //startActivityForResult(it, 1);
//				for(Map<String,Object> map:picList){
//					imgList.add(map.get("addr").toString());
//				}
				Intent it = new Intent(NewAsk.this,
						MultiPicSelect.class);
				//Bundle bundle = new Bundle();
				//bundle.putStringArrayList("dataList",(ArrayList<String>) imgList);
				it.putExtra("num", 9-picList.size());
				startActivityForResult(it, 2);
				break;
			case 2:
				if(emotionBoxShow){
					emotionBoxShow=false;
					emotionBox.setVisibility(View.GONE);
				}else{
					emotionBoxShow=true;
					emotionBox.setVisibility(View.VISIBLE);
				}
				InputHelper.Hide(NewAsk.this);
				break;
			default:
				break;
			}
		}
		
	}

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		switch(buttonId){
		case 1:
			finish();
			break;
		case 2:
			if(!lh.hasLogin()){
				T.showLong(NewAsk.this,"请登陆后再发表动态");
			}else if(picList.size()==0&&input.getText().toString().trim().length()==0){
				T.showShort(NewAsk.this,"发布的内容不能为空哦");
			}else{
				mpb=new MyProgressBar(NewAsk.this);
				mpb.setMessage("正在提交...");
				
				RequestParams params = new RequestParams();
				
				for(int k=1;k<picList.size()+1;k++){
					File f=new File(picList.get(k-1).get("addr").toString());
					if(f.exists()){
						params.addBodyParameter("pictures_"+k,f);
					}
				}
				//上传动态内容
				String textVal=input.getText().toString();
				if(textVal.trim().length()==0){
					textVal="#照片分享#";
				}
				params.addBodyParameter("num",String.valueOf(picList.size()));
				params.addBodyParameter("content",textVal);
				params.addBodyParameter("uid",lh.getUid());
				params.addBodyParameter("token",lh.getToken());
				
				HttpUtils http = new HttpUtils();
				http.send(HttpMethod.POST, ServerConfig.HOST+"/schoolknow/Square/uploadFile.php",
						params,new RequestCallBack<String>() {
							public void onSuccess(ResponseInfo<String> info) {
								mpb.dismiss();
								picList.clear();
								input.setText("");
								T.showShort(NewAsk.this,"发布成功");
								Intent it=new Intent(NewAsk.this,SchoolFellowSquare.class);
								startActivity(it);
								finish();
							}

							public void onFailure(HttpException arg0, String arg1) {
								mpb.dismiss();
								T.showShort(NewAsk.this,"发布动态出现错误,请重新尝试+"+arg1);
							}
						});
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	@SuppressLint("SimpleDateFormat")
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		 ContentResolver resolver = getContentResolver();
		 if(requestCode == 1){
			 if(data != null){
				// 获得图片的uri
                 Uri originalUri = data.getData();
                 try {
					mContent = SelectPicUtil.readStream(resolver.openInputStream(Uri.parse(originalUri.toString())));
				} catch (Exception e) {
					T.showShort(NewAsk.this, "获取图片错误");
                   return;
				}
                myBitmap = SelectPicUtil.getPicFromBytes(mContent, null);  
                
                //获取图片的实际地址
                String[] proj = { MediaColumns.DATA };
                Cursor actualimagecursor = managedQuery(originalUri,proj,null,null,null);
                int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaColumns.DATA);
                actualimagecursor.moveToFirst(); 
                picAddr= actualimagecursor.getString(actual_image_column_index);
			 }
		 }else if(requestCode == 0){
			 String path=PathConfig.SavePATH+picName;
			  File temp = new File(path);
			  if(temp.exists()){
				  myBitmap=BitmapFactory.decodeFile(path);
				  picAddr=path;
			  }else{
				  return;
			  }
		 }else if(requestCode == 2){
			 if (resultCode == RESULT_OK) {
					Bundle bundle = data.getExtras();
					@SuppressWarnings("unchecked")
					final ArrayList<String> tDataList = (ArrayList<String>) bundle
							.getSerializable("dataList");
					if (tDataList != null) {
						waitLoadPic=new MyProgressBar(NewAsk.this);
						waitLoadPic.setMessage("正在加载中...");
						new Thread(new Runnable() {
							public void run() {
								for(String str:tDataList){
									 Map<String,Object> map=new HashMap<String, Object>();
									 Bitmap bm = BitmapFactory.decodeFile(str);
									 map.put("bm",bm);
									 //压缩图片
									 String temp=PictureUtil.saveSmallPic(str);
									 map.put("addr",temp);
									 picList.add(map);
								}
								Message msg=new Message();
								msg.what=104;
								handler.sendMessage(msg);
							}
						}).start();
						return;
					}
				}
		 }
		 //显示图片
		 if(myBitmap!=null&&picAddr.trim().length()!=0){
			  for(Map<String,Object> map2:picList){
 				  if(picAddr.equals(map2.get("addr"))){
 					T.showShort(NewAsk.this, "该图片已经存在请勿重复选取");
 					return;
 				  }
 			  }
			  //压缩图片
			  picAddr=PictureUtil.saveSmallPic(picAddr);
			  
			  //存储图片
 			  Map<String,Object> map=new HashMap<String, Object>();
			  map.put("bm",myBitmap);
			  map.put("addr",picAddr);
 			  if(picList.size()>=9){
 				T.showShort(NewAsk.this, "上传不允许超过9张图片");
 			  }else{
 				 picList.add(map);
				 gvAdapter.notifyDataSetChanged();
 			  }
		 }
	}
	
	//选择多张返回时刷新页面
	Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what==104){
				gvAdapter.notifyDataSetChanged();
				waitLoadPic.dismiss();
			}
			super.handleMessage(msg);
		}
		
	};
	
	
	
	

}
