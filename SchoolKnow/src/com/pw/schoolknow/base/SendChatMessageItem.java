package com.pw.schoolknow.base;

import java.io.Serializable;

import com.google.gson.annotations.Expose;


/**
 * 发送聊天内容封装类
 * @author peng
 *
 */
public class SendChatMessageItem implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Expose
	private int type;
	@Expose
	private long time;
	@Expose
	private String content;
	
	public SendChatMessageItem(int type, long time, String content) {
		super();
		this.type = type;
		this.time = time;
		this.content = content;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	

}
