package com.pw.schoolknow.base;

import java.io.Serializable;



public class IndexItemBase implements Comparable<IndexItemBase>,Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int INFO_EVENT=1;
	public static final int INFO_NEWS=2;
	public static final int INFO_INFROM=3;
	public static final int INFO_READ=4;
	
	private int id;
	private int type;
	private String title;
	private String intro;
	private String address;
	private long timesamp;
	private String param;
	
	public IndexItemBase(int id,int type,String title, String intro, String address, long time,String param) {
		super();
		this.id=id;
		this.title = title;
		this.intro = intro;
		this.address = address;
		this.timesamp = time;
		this.type=type;
		this.param=param;
	}
	
	
	

	public int getId() {
		return id;
	}




	public void setId(int id) {
		this.id = id;
	}




	public String getParam() {
		return param;
	}



	public void setParam(String param) {
		this.param = param;
	}



	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}



	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}



	public long getTimesamp() {
		return timesamp;
	}



	public void setTimesamp(long timesamp) {
		this.timesamp = timesamp;
	}



	@Override
	public int compareTo(IndexItemBase item) {
		return this.getTimesamp()>=item.getTimesamp()?-1:1; 
	}
	
	

	
	
	
	

}
