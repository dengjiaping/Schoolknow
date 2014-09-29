package com.pw.schoolknow.activity;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.pw.schoolknow.R;
import com.pw.schoolknow.config.PathConfig;
import com.pw.schoolknow.utils.BitmapUtil;
import com.pw.schoolknow.utils.GetUtil;
import com.pw.schoolknow.utils.Sha1Util;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.widgets.ZoomImageView;

public class ImgPreview extends Activity {
	private ZoomImageView img;
	private ImageButton save;
	private String imgsrc;
	private RelativeLayout layout;
	
	private Bitmap CurrentBm;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_img_preview);
		
		imgsrc=getIntent().getStringExtra("imgsrc");
		
		img=(ZoomImageView) super.findViewById(R.id.img_preview);
		
		
		layout=(RelativeLayout) super.findViewById(R.id.img_load_layout);
		
		//下载图片到本地
		save=(ImageButton) super.findViewById(R.id.preview_btn_save);
		save.setOnClickListener(new saveImageToLocal());
		
		//显示图片
		new Thread(new Runnable(){
			@Override
			public void run() {
				Bitmap bm=GetUtil.getBitMap(imgsrc);
				Message msg=new Message();
				msg.what=102;
				msg.obj=bm;
				handler.sendMessage(msg);				
		}}).start();
				
	}
	
	/**
	 * 图片下载
	 * @author wei8888go
	 *
	 */
	public class saveImageToLocal implements OnClickListener{
		@Override
		@SuppressLint("SdCardPath")
		public void onClick(View v) {
			String imageName = new Sha1Util().getDigestOfString(imgsrc.getBytes());
			//获取文件后缀名
			String[] ss = imgsrc.split("\\.");
			String ext = ss[ss.length - 1];
			
			// 最终图片保持的地址
			String savePath = PathConfig.SavePATH + imageName + "." + ext;
			
			File file = new File(savePath);
			
			if (file.exists()) {
				// 如果文件已经存在
				T.showShort(ImgPreview.this,"该图片已经存在于SD卡schoolknow文件夹中");
				return;
			}		
			try {
				if(CurrentBm!=null){
					BitmapUtil.saveImg(CurrentBm,PathConfig.SavePATH,imageName + "." + ext);
					T.showShort(ImgPreview.this,"图片已保存到SD卡的schoolknow文件夹里!");
				}
			} catch (Exception e) {
				T.showShort(ImgPreview.this,"保存失败");
			}
		}
		
	}
	
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
            if(msg.what == 102){
            	CurrentBm=(Bitmap) msg.obj;
            	layout.setVisibility(View.GONE);
            	img.setVisibility(View.VISIBLE);
    			img.setImageBitmap(CurrentBm);
            }
        };
    };
	

	protected void HandleTitleBarEvent(int buttonId, View v) {
		switch(buttonId){
		case 0:
			finish();
			break;
		case 1:
			break;
		default:
			break;
		}

	}


}
