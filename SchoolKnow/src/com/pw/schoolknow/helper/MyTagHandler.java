package com.pw.schoolknow.helper;


import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.XMLReader;





import com.pw.schoolknow.activity.FriendZone;
import com.pw.schoolknow.activity.ImgPreview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.Html.TagHandler;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.Toast;


public class MyTagHandler implements TagHandler {
	private Context context;
	
	private int startIndex = 0;
	private int stopIndex = 0;
	private int countItemLinks = 0;
	List<String> ids = new ArrayList<String>();
	
	public MyTagHandler(Context context) {
		this.context = context;
	}
	
	public MyTagHandler(Context context,String html) {
		this.context = context;
		Document doc = Jsoup.parse(html);
		Elements links = doc.select("at");
		for (Element link : links) {
			ids.add(link.attr("id"));
		}
	}
	
	@SuppressLint("DefaultLocale")
	@Override
	public void handleTag(boolean opening, String tag, Editable output,
			XMLReader xmlReader) {
		if (tag.toLowerCase().equals("at")) {
			if (opening) {
				startIndex = output.length();
			} else {
				stopIndex = output.length();
				output.setSpan(new NickClick(countItemLinks), startIndex,
						stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				output.setSpan(new NoLineClickSpan(), startIndex, stopIndex,
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);                      //下划线
				//output.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
				//		startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 粗体
				output.setSpan(new ForegroundColorSpan(Color.parseColor("#396CA4")),
						startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //前景色
				

				countItemLinks++;
			}
		}
		if(tag.toLowerCase().equals("img")){
			int len = output.length();
			ImageSpan[] images = output.getSpans(len-1, len, ImageSpan.class);
			String imgURL = images[0].getSource();
			output.setSpan(new ImageClick(context,imgURL), len-1, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		
	}
	
	/**
	 * 处理@标签
	 * @author wei8888go
	 *
	 */
	public class NickClick extends ClickableSpan{
		
		public int tagLinkIndex;

		public NickClick() {
			tagLinkIndex=0;
		}

		public NickClick(int linkIndex) {
			this.tagLinkIndex = linkIndex;
		}
		
		public void onClick(View v) {
			Intent it=new Intent(context,FriendZone.class);
			it.putExtra("uid", ids.get(tagLinkIndex));
			context.startActivity(it);
		}
		
	}
	
	/**
	 * 处理img标签
	 * @author wei8888go
	 *
	 */
	private class ImageClick extends ClickableSpan{

		private String url;
		private Context context;
		
		public ImageClick(Context context, String url) {
			this.context = context;
			this.url = url;
		}
		
		public void onClick(View v) {
			Intent it=new Intent(context,ImgPreview.class);
			it.putExtra("imgsrc",url);
			context.startActivity(it);
			
		}
		
	}
	
	
	//无下划线超链接，使用textColorLink、textColorHighlight分别修改超链接前景色和按下时的颜色
	private class NoLineClickSpan extends ClickableSpan { 
		
	    public NoLineClickSpan() {}

	    @Override
	    public void updateDrawState(TextPaint ds) {
	        ds.setColor(ds.linkColor);
	        ds.setUnderlineText(false);//去掉下划线</span>
	    }

		public void onClick(View arg0) {
			
		}
	}

}
