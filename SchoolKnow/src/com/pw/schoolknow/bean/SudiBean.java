package com.pw.schoolknow.bean;

public class SudiBean {
	
	public int id;
	private long time;
	private String bookid;
	private String sudiCode;
	private String sudiName;
	private String content;
	
	public SudiBean(){
		
	}
	
	public SudiBean(long time, String bookid, String sudiCode, String sudiName,
			String content) {
		super();
		this.time = time;
		this.bookid = bookid;
		this.sudiCode = sudiCode;
		this.sudiName = sudiName;
		this.content = content;
		this.id=0;
	}

	public SudiBean(int id, long time, String bookid, String sudiCode,
			String sudiName, String content) {
		super();
		this.id = id;
		this.time = time;
		this.bookid = bookid;
		this.sudiCode = sudiCode;
		this.sudiName = sudiName;
		this.content = content;
	}

	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getBookid() {
		return bookid;
	}
	public void setBookid(String bookid) {
		this.bookid = bookid;
	}
	public String getSudiCode() {
		return sudiCode;
	}
	public void setSudiCode(String sudiCode) {
		this.sudiCode = sudiCode;
	}
	public String getSudiName() {
		return sudiName;
	}
	public void setSudiName(String sudiName) {
		this.sudiName = sudiName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	

}
