package com.pw.schoolknow.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ToggleButton;

import com.pw.schoolknow.R;
import com.pw.schoolknow.adapter.AlbumGridViewAdapter;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.utils.ImageManager2;
import com.pw.schoolknow.utils.T;

public class MultiPicSelect extends BaseActivity {
	
	private GridView gridView;
	private ArrayList<String> dataList = new ArrayList<String>();
	private HashMap<String,ImageView> hashMap = new HashMap<String,ImageView>();
	private ArrayList<String> selectedDataList = new ArrayList<String>();
	private ProgressBar progressBar;
	private AlbumGridViewAdapter gridImageAdapter;
	private LinearLayout selectedImageLayout;
	private HorizontalScrollView scrollview;
	private int size = 0 ;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_multi_pic_select);
		setTitle("图片选择");
		Intent intent = getIntent();
		size = intent.getIntExtra("num", 0) ;
		//Bundle bundle = intent.getExtras();
		//selectedDataList = (ArrayList<String>)bundle.getSerializable("dataList");
		selectedDataList=new ArrayList<String>();
		init();
		initListener();
	}
	
	private void init() {
		progressBar = (ProgressBar)findViewById(R.id.multi_pic_select_progressbar);
		progressBar.setVisibility(View.GONE);
		gridView = (GridView)findViewById(R.id.multi_pic_select_myGrid);
		gridImageAdapter = new AlbumGridViewAdapter(this, dataList,selectedDataList);
		gridView.setAdapter(gridImageAdapter);
		refreshData();
		selectedImageLayout = (LinearLayout)findViewById(R.id.multi_pic_selected_image_layout);
		scrollview = (HorizontalScrollView)findViewById(R.id.multi_pic_select_scrollview);
		initSelectImage();
	}
	
	private void initSelectImage() {
		if(selectedDataList==null)
			return;
		for(final String path:selectedDataList){
			ImageView imageView = (ImageView) LayoutInflater.from(MultiPicSelect.this).inflate(R.layout.item_choose_imageview, selectedImageLayout,false);
			selectedImageLayout.addView(imageView);			
			hashMap.put(path, imageView);
			ImageManager2.from(MultiPicSelect.this).displayImage(imageView,path,R.drawable.empty_photo,100,100);
			imageView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					removePath(path);
					gridImageAdapter.notifyDataSetChanged();
				}
			});
		}
		setTitleBar(0,"相册",0,"完成("+selectedDataList.size()+"/"+size+")");
	}
	
private void initListener() {
		
		gridImageAdapter.setOnItemClickListener(new AlbumGridViewAdapter.OnItemClickListener() {
			
			@Override
	public void onItemClick(final ToggleButton toggleButton, int position, final String path,boolean isChecked) {
				if(selectedDataList.size()>=size){
					toggleButton.setChecked(false);
					if(!removePath(path)){
						T.showShort(MultiPicSelect.this, "只能选择"+size+"张图片");
					}
					return;
				}
				ViewGroup pGroup = (ViewGroup) toggleButton.getParent() ;
				final ImageView check = (ImageView) pGroup.findViewById(R.id.check) ;
				if(isChecked){
					check.setVisibility(View.VISIBLE) ;
					if(!hashMap.containsKey(path)){
						ImageView imageView = (ImageView) LayoutInflater.from(MultiPicSelect.this).inflate(R.layout.item_choose_imageview, selectedImageLayout,false);
						selectedImageLayout.addView(imageView);
						imageView.postDelayed(new Runnable() {
							
							@Override
							public void run() {
								
								int off = selectedImageLayout.getMeasuredWidth() - scrollview.getWidth();  
							    if (off > 0) {  
							    	  scrollview.smoothScrollTo(off, 0); 
							    } 
							}
						}, 100);
						
						hashMap.put(path, imageView);
						selectedDataList.add(path);
						ImageManager2.from(MultiPicSelect.this).displayImage(imageView, path,R.drawable.empty_photo,100,100);
						imageView.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								toggleButton.setChecked(false);
								removePath(path);
								check.setVisibility(View.GONE) ;
							}
						});
						setTitleBar(0,"相册",0,"完成("+selectedDataList.size()+"/"+size+")");
					}
				}else{
					check.setVisibility(View.GONE) ;
					removePath(path);
				}
			}
		});
		
//		okButton.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				
//				Intent intent = new Intent();
//				Bundle bundle = new Bundle();
//				// intent.putArrayListExtra("dataList", dataList);
//				bundle.putStringArrayList("dataList",selectedDataList);
//				intent.putExtras(bundle);
//				setResult(RESULT_OK, intent);
//				finish();
//			}
//		});
		
	}
	
	private boolean removePath(String path){
		if(hashMap.containsKey(path)){
			selectedImageLayout.removeView(hashMap.get(path));
			hashMap.remove(path);
			removeOneData(selectedDataList,path);
			setTitleBar(0,"相册",0,"完成("+selectedDataList.size()+"/"+size+")");
			return true;
		}else{
			return false;
		}
	}
	
	private void removeOneData(ArrayList<String> arrayList,String s){
		for(int i =0;i<arrayList.size();i++){
			if(arrayList.get(i).equals(s)){
				arrayList.remove(i);
				return;
			}
		}
	}
	
    private void refreshData(){
    	
    	new AsyncTask<Void, Void, ArrayList<String>>(){
    		
    		@Override
    		protected void onPreExecute() {
    			progressBar.setVisibility(View.VISIBLE);
    			super.onPreExecute();
    		}

			@Override
			protected ArrayList<String> doInBackground(Void... params) {
				ArrayList<String> listDirlocal =  listAlldir();
				return listDirlocal;
			}
			
			protected void onPostExecute(ArrayList<String> tmpList) {
				
				if(MultiPicSelect.this==null||MultiPicSelect.this.isFinishing()){
					return;
				}
				progressBar.setVisibility(View.GONE);
				dataList.clear();
				dataList.addAll(tmpList);
				gridImageAdapter.notifyDataSetChanged();
				return;
				
			};
    		
    	}.execute();
    	
    }
    
    
    /**
     * 获取图库图片所有路径
     * @return
     */
    private ArrayList<String>  listAlldir(){
    	Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    	Uri uri = intent.getData();
    	ArrayList<String> list = new ArrayList<String>();
    	String[] proj ={MediaStore.Images.Media.DATA};
    	Cursor cursor = managedQuery(uri, proj, null, null, null);
    	while(cursor.moveToNext()){
    		String path =cursor.getString(0);
    		list.add(new File(path).getAbsolutePath());
    	}
		return list;
    }
        
    @Override
    public void onBackPressed() {
    	finish();
//    	super.onBackPressed();
    }
    
    @Override
    public void finish() {
    	// TODO Auto-generated method stub
    	super.finish();
//    	ImageManager2.from(AlbumActivity.this).recycle(dataList);
    }
    
    @Override
    protected void onDestroy() {
    	
    	super.onDestroy();
    }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//ContentResolver resolver = getContentResolver();
		 if(requestCode == 1){
			 if(data != null){
				 Uri originalUri = data.getData();
				 try {
					//byte[] mContent=SelectPicUtil.readStream(resolver.openInputStream(Uri.parse(originalUri.toString())));
					String[] proj = { MediaColumns.DATA };
		            Cursor actualimagecursor = managedQuery(originalUri,proj,null,null,null);
		            int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaColumns.DATA);
		            actualimagecursor.moveToFirst(); 
		            String picAddr= actualimagecursor.getString(actual_image_column_index);
		            selectedDataList.add(picAddr);
		            initSelectImage();
				}catch (Exception e) {
				}
			 }
		 }
	}

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		switch(buttonId){
		case 1:
			Intent it = new Intent(
	              Intent.ACTION_PICK,
	              android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(it, 1);
			break;
		case 2:
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putStringArrayList("dataList",selectedDataList);
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent);
			finish();
			break;
		default:
			break;
		}
	}

}
