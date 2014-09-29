package com.pw.schoolknow.widgets;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;

import com.pw.schoolknow.R;
import com.pw.schoolknow.adapter.EmotionAdapter;
import com.pw.schoolknow.adapter.ViewPagerAdapter;
import com.pw.schoolknow.config.EmotionConfig;

/**
 * 表情框
 * @author wei8888go
 *
 */
public class EmotionBox {
	
	private ImageButton emotionBtn;
	private ViewPager df_vp=null;
	private LinearLayout df_dot_layout;
	private List<View> default_emotion_list=null;
	private int currentIndex;
	private ImageView[] dots ;
	private List<String> keys;
	private EditText input;
	private LinearLayout emotionBox;
	private Button submitBtn;
	
	private Activity act;
	
	private boolean hasShow=false;
	
	//事件传递接口
	public interface EmotionBoxClickListener{  
        public void onClick(String EditTextVal,View view);  
    } 
	
	public EmotionBoxClickListener callBack = null; 
	
	public void setOnClick(EmotionBoxClickListener callBack){
		this.callBack=callBack;
	}
	

	public EmotionBox(final Activity act) {
		this.currentIndex =0;
		this.act=act;
		input=(EditText) act.findViewById(R.id.chat_inputbox_edit);
		df_vp=(ViewPager) act.findViewById(R.id.default_emotion_viewpager);
		df_dot_layout=(LinearLayout) act.findViewById(R.id.default_emotion__viewpager_dot);
		emotionBtn=(ImageButton) act.findViewById(R.id.chat_inputbox_emotion);
		emotionBox=(LinearLayout) act.findViewById(R.id.chat_emotionbox);
		submitBtn=(Button) act.findViewById(R.id.chat_inputbox_submit);
		
		Set<String> keySet =new EmotionConfig().getFaceMap()
				.keySet();
		keys = new ArrayList<String>();
		keys.addAll(keySet);
		
		initDefaultEmotion();
		
		emotionBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View V) {
				if(hasShow){
					hasShow=false;
					emotionBox.setVisibility(View.GONE);
				}else{
					hasShow=true;
					emotionBox.setVisibility(View.VISIBLE);
				}
				Context context=act.getApplication();
				InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE); 
		        imm.hideSoftInputFromWindow(input.getWindowToken(),0);	
			}
		});
		submitBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				callBack.onClick(getValue(),v);
			}
		});
		/**
		 * 输入框检测
		 */
		input.addTextChangedListener(new TextWatcher(){
			@Override
			public void afterTextChanged(Editable arg0) {
				if(input.getText().toString().trim().length()==0){
					submitBtn.setEnabled(false);
					submitBtn.setTextColor(Color.GRAY);
				}else{
					submitBtn.setEnabled(true);
					submitBtn.setTextColor(Color.WHITE);
				}
			}
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
		});
		
		//点击输入框隐藏表情框
		input.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean flag) {
				if(flag){
					if(hasShow){
						emotionBox.setVisibility(View.GONE);
						hasShow=false;
					}
				}
			}
		});
		
		//点击输入框隐藏表情框
		input.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(hasShow){
					emotionBox.setVisibility(View.GONE);
					hasShow=false;
				}
			}
		});
	}
	
	
	//获取输入框的内容
	public String getValue(){
		return input.getText().toString();
	}
	//设置输入框的内容
	public void SetValue(String val){
		input.setText(val);
	}
	
	//设置是否允许点击
	public void setClickable(boolean b){
		submitBtn.setClickable(b);
	}
	
	//隐藏输入法和表情框
	public void HideEmotionBox(){
		//隐藏表情框
		hasShow=false;
		emotionBox.setVisibility(View.GONE);
		
		//隐藏输入法
		Context context=act.getApplication();
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE); 
        imm.hideSoftInputFromWindow(input.getWindowToken(),0);	
	}
	
	//设置EditText的Hint值
	public void setEditHint(String val){
		input.setHint(val);
	}
	
	public void initDefaultEmotion(){
		dots = new ImageView[EmotionConfig.NUM_PAGE];
		default_emotion_list = new ArrayList<View>();
		for (int i = 0; i < EmotionConfig.NUM_PAGE; ++i){
			default_emotion_list.add(getGridView(act.getApplication(),i,input));
			dots[i]=new ImageView(act.getApplication());
			dots[i].setEnabled(false);
			dots[i].setImageResource(R.drawable.dot_2);
			LinearLayout.LayoutParams param=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			 param.setMargins(10, 0, 10,0);
			 df_dot_layout.addView(dots[i],param);
		}
	    dots[currentIndex].setEnabled(true);
		df_vp.setAdapter(new ViewPagerAdapter(act.getApplication(),default_emotion_list));
		df_vp.setOnPageChangeListener(new setOnPageChangeListenerImp());
		
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

	/**
	 * 默认表情分页
	 * @param i
	 * @return
	 */
	public  GridView getGridView(final Context context,int i,final EditText input) {
		GridView gv = new GridView(context);
		gv.setNumColumns(7);
		gv.setSelector(new ColorDrawable(Color.TRANSPARENT));// 屏蔽GridView默认点击效果
		gv.setBackgroundColor(Color.TRANSPARENT);
		gv.setCacheColorHint(Color.TRANSPARENT);
		gv.setHorizontalSpacing(7);
		gv.setVerticalSpacing(12);
		gv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		gv.setGravity(Gravity.CENTER);
		gv.setAdapter(new EmotionAdapter(context,i));
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
							context.getResources(), (Integer) new EmotionConfig().getFaceMap().values()
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
						ImageSpan imageSpan = new ImageSpan(context,
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
	
	// 防止乱pageview乱滚动
 	public static OnTouchListener forbidenScroll() {
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
 	
 	/**
	 * 另外一种方法解析表情
	 * @param message
	 * @return
	 */
	public static CharSequence convertNormalStringToSpannableString(Context context,String message) {
		String hackTxt;
		Pattern EMOTION_URL = Pattern.compile("\\[(\\S+?)\\]");
		if (message.startsWith("[") && message.endsWith("]")) {
			hackTxt = message + " ";
		} else {
			hackTxt = message;
		}
		SpannableString value = SpannableString.valueOf(hackTxt);

		Matcher localMatcher = EMOTION_URL.matcher(value);
		while (localMatcher.find()) {
			String str2 = localMatcher.group(0);
			int k = localMatcher.start();
			int m = localMatcher.end();
			// k = str2.lastIndexOf("[");
			// Log.i("way", "str2.length = "+str2.length()+", k = " + k);
			// str2 = str2.substring(k, m);
			if (m - k < 8) {
				if (new EmotionConfig().getFaceMap()
						.containsKey(str2)) {
					int face = new EmotionConfig().getFaceMap()
							.get(str2);
					Bitmap bitmap = BitmapFactory.decodeResource(
							context.getResources(), face);
					if (bitmap != null) {
						ImageSpan localImageSpan = new ImageSpan(context,
								bitmap, DynamicDrawableSpan.ALIGN_BASELINE);
						value.setSpan(localImageSpan, k, m,
								Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
				}
			}
		}
		return value;
	}

}
