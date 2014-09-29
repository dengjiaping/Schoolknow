package com.pw.schoolknow.activity;

import java.io.File;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pw.schoolknow.R;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.utils.UploadFileUtil;

public class FileSharingUpload extends BaseActivity {
	
	private Button select;
	private Button submit;
	private TextView tip;
	private EditText edit;
	private String FileUrl=null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_file_sharing_upload);
		setTitle("共享文件上传");
		setTitleBar(R.drawable.btn_titlebar_back,0);
		
		this.select=(Button) super.findViewById(R.id.sharing_upload_select);
		this.edit=(EditText) super.findViewById(R.id.sharing_upload_updata);
		this.tip=(TextView) super.findViewById(R.id.sharing_upload_tip);
		this.submit=(Button) super.findViewById(R.id.sharing_upload_submit);
		
		//跳转到文件选择界面
		this.select.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent it=new Intent(FileSharingUpload.this,FileSelect.class);
				startActivity(it);				
			}
		});
		
		submit.setOnClickListener(new submitListener());
		
		String action=getIntent().getStringExtra("action");
		//不是从选择文件界面跳转过来的
		if(action.equals("false")){
			return;
		}else{
			FileUrl=getIntent().getStringExtra("path");
			try{
				String fileName = FileUrl.substring(FileUrl.lastIndexOf("/")+1,
						FileUrl.lastIndexOf("."));
				tip.setText("你选择的文件是:"+FileUrl+",可以修改上传文件名");
				edit.setVisibility(View.VISIBLE);
				edit.setText(fileName);
			}catch(Exception e){
				T.showLong(FileSharingUpload.this,"你选择的文件格式不明确，请重新选择");
			}
			
		}
		
		
	}
	
	//上传文件
	public class submitListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			File f=new File(FileUrl);
			if(FileUrl==null){
				T.showShort(FileSharingUpload.this, "请先选择文件在上传");
			}else if(!f.isFile()){
				T.showShort(FileSharingUpload.this, "你选择的文件有误");
			}else if(f.length()/(1024*1024)>20){
				T.showShort(FileSharingUpload.this, "上传的文件不能大于20M");
			}else if(edit.getText().toString().equals("")){
				T.showShort(FileSharingUpload.this, "上传的文件名不能为空");
			}else if(edit.getText().length()>30){
				T.showShort(FileSharingUpload.this, "上传的文件名不能长于30个字符");
			}else{
				new UploadFileUtil(FileSharingUpload.this,FileUrl).execute();
				tip.setText("请选择你要共享的文件");
				edit.setText("");
				edit.setVisibility(View.GONE);
			}
		}
		
	}

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		// TODO Auto-generated method stub

	}

}
