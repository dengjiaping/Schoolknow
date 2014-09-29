package com.pw.schoolknow.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.pw.schoolknow.R;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.utils.T;



public class FileSelect extends BaseActivity{
	
	private ListView listview=null;
	private List<Map<String,Object>> fileItem=new ArrayList<Map<String,Object>>();
	private ListFileThread ft=null;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_file_select);
		setTitle("选择文件");
		setTitleBar(R.drawable.btn_titlebar_back,0);
		
		this.listview=(ListView) super.findViewById(R.id.file_select_listview);
		this.listview.setOnItemClickListener(new OnItemClickListenerimp());
		
		//File filePath=new File(java.io.File.separator);   //从根目录开始列出
		File filePath=new File("/storage/");
		this.ft=new ListFileThread();
		this.ft.execute(filePath);
	}
	
	
	
	public class OnItemClickListenerimp implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			File currentFile=(File) fileItem.get(position).get("name");
			if(currentFile.isDirectory()){
				String folder=currentFile.getPath();
				
				//根目录下一些禁止访问的文件夹
				if((folder.equals("/root")||folder.equals("/data")||folder.equals("/config")
						||folder.equals("/cache")||folder.equals("/persist")||folder.equals("/sbin")
						||folder.equals("/sd-ext")||folder.equals("/tmp"))){
					T.showShort(FileSelect.this, "没有权限访问该文件夹");
					return;					
				}
				fileItem.clear();
				ft=new ListFileThread();
				ft.execute(currentFile);
			}else{
				//跳转到文件选择页面
				Intent it=new Intent(FileSelect.this,FileSharingUpload.class);
				it.putExtra("path",currentFile.getPath());
				it.putExtra("action","true");
				startActivity(it);				
			}
			
		}
		
	}

	@Override
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
	
	
	private class ListFileThread extends AsyncTask<File, File, String>{

		
		@Override
		protected void onProgressUpdate(File... values) {
			Map<String,Object> map=new HashMap<String, Object>();
			if(values[0].isDirectory()){
				map.put("img", R.drawable.file_close);
			}else{
				map.put("img", R.drawable.default_fileicon);
			}
			map.put("name", values[0]);
			fileItem.add(map);
			listview.setAdapter(new FileAdapter(FileSelect.this, fileItem));
		}

		@Override
		protected String doInBackground(File... params) {
			//不是根目录
			if(!params[0].getPath().equals(java.io.File.separator)){
				Map<String,Object> map=new HashMap<String, Object>();
				map.put("img", R.drawable.file_open);
				map.put("name",params[0].getParentFile());
				fileItem.add(map);
			}
			if(params[0].isDirectory()){
				File[] tempFile=params[0].listFiles();
				if(tempFile!=null){
					for(int i=0;i<tempFile.length;i++){
						this.publishProgress(tempFile[i]);
					}
				}else{
					T.showShort(FileSelect.this, "该文件夹为空");
					return null;
				}				
			}
			return null;
		}
		
	}
	
	
	/**
	 * 文件选择适配器:为不同的文件格式匹配不一样的图片
	 * @author wei8888go
	 *
	 */
	public class FileAdapter extends BaseAdapter{
		private List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		private LayoutInflater mInflater=null;
		
		public FileAdapter(Context context,List<Map<String,Object>> list){
			this.list=list;
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
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

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v=null;
			if(convertView==null){
				v=mInflater.inflate(R.layout.item_file_select_lv,null);
			}else{
				v=convertView;
			}
			ImageView img=(ImageView) v.findViewById(R.id.file_select_img);
			TextView name=(TextView) v.findViewById(R.id.file_select_title);
			Map<String,Object> map=new HashMap<String, Object>();
			map=list.get(position);
			String path=getFileName(map.get("name").toString());
			if(getFileName(path).equals("")){
				name.setText("返回上一级目录");
			}else{
				name.setText(getFileName(path));
			}
			
			String type=getFileType(path);
			if(type.equals("doc")||type.equals("docx")){
				img.setImageResource(R.drawable.doc);
			}else if(type.equals("xls")||type.equals("xlsx")){
				img.setImageResource(R.drawable.xls);
			}else if(type.equals("ppt")||type.equals("pptx")){
				img.setImageResource(R.drawable.ppt);
			}else if(type.equals("pdf")){
				img.setImageResource(R.drawable.pdf);
			}else if(type.equals("txt")){
				img.setImageResource(R.drawable.txt);
			}else if(type.equals("zip")||type.equals("rar")||type.equals("7z")){
				img.setImageResource(R.drawable.file_archive);
			}else if(type.equals("bmp")||type.equals("png")||type.equals("jpeg")||type.equals("jpg")||type.equals("gif")){
				img.setImageResource(R.drawable.file_image);
			}else if(type.equals("mp3")||type.equals("wav")||type.equals("ogg")){
				img.setImageResource(R.drawable.file_audio);
			}else if(type.equals("avi")||type.equals("mp4")||type.equals("rmvb")||type.equals("rm")||type.equals("3gp")){
				img.setImageResource(R.drawable.file_video);
			}else{
				img.setImageResource((Integer)map.get("img"));
			}
			
			return v;
		}
		
		public String getFileName(String path){
			File tempFile =new File(path.trim());          
			return tempFile.getName();
		}
		
		public String getFileType(String path){
			String filename=getFileName(path);
			if ((filename != null) && (filename.length() > 0)) {   
	            int dot = filename.lastIndexOf('.');   
	            if ((dot >-1) && (dot < (filename.length() - 1))) {   
	                return filename.substring(dot + 1);   
	            }   
	        }
	        return filename;   
		}
		
		
		
	}
	
	

}
