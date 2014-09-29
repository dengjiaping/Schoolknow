package com.pw.schoolknow.base;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

/**
 * 消息通知基类
 * @author wei8888go
 *
 */
public class InformBase implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public int id;
	
	@Expose
	public int type;
	@Expose
	public String sendUid;
	@Expose
	public String sendName;
	@Expose
	public String content;
	@Expose
	public String time;
	@Expose
	public String targetId;
	@Expose
	public String receiveUid;
	@Expose
	public String clientId;
	
	public InformBase(int type, String sendUid, String sendName,
			String content, String time) {
		this.type = type;
		this.sendUid = sendUid;
		this.sendName = sendName;
		this.content = content;
		this.time = time;
		targetId="1";
		this.receiveUid="";
		clientId="0";
	}
	
	
	public InformBase(int type, String sendUid, String sendName,
			String content, String time,String targetId) {
		this.type = type;
		this.sendUid = sendUid;
		this.sendName = sendName;
		this.content = content;
		this.time = time;
		this.targetId=targetId;
		this.receiveUid="";
		clientId="0";
	}
	
	public InformBase(int type, String sendUid, String sendName,
			String content, String time,String targetId,String receiveUid) {
		this.type = type;
		this.sendUid = sendUid;
		this.sendName = sendName;
		this.content = content;
		this.time = time;
		this.targetId=targetId;
		this.receiveUid=receiveUid;
		clientId="0";
	}
	
	public InformBase(int type, String sendUid, String sendName,
			String content, String time,String targetId,String receiveUid,String clientId) {
		this.type = type;
		this.sendUid = sendUid;
		this.sendName = sendName;
		this.content = content;
		this.time = time;
		this.targetId=targetId;
		this.receiveUid=receiveUid;
		this.clientId=clientId;
	}
	
	public InformBase(int id,int type, String sendUid, String sendName,
			String content, String time) {
		this.id=id;
		this.type = type;
		this.sendUid = sendUid;
		this.sendName = sendName;
		this.content = content;
		this.time = time;
		targetId="1";
		clientId="0";
	}
	
	public InformBase(int id,int type, String sendUid, String sendName,
			String content, String time,String targetId) {
		this.id=id;
		this.type = type;
		this.sendUid = sendUid;
		this.sendName = sendName;
		this.content = content;
		this.time = time;
		this.targetId=targetId;
		clientId="0";
	}
	
	public String getClientId() {
		return clientId;
	}


	public void setClientId(String clientId) {
		this.clientId = clientId;
	}


	public String getReceiveUid() {
		return receiveUid;
	}


	public void setReceiveUid(String receiveUid) {
		this.receiveUid = receiveUid;
	}


	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getSendUid() {
		return sendUid;
	}

	public void setSendUid(String sendUid) {
		this.sendUid = sendUid;
	}

	public String getSendName() {
		return sendName;
	}

	public void setSendName(String sendName) {
		this.sendName = sendName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	
	
	
	
	
	
}
