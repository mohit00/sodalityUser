package com.radius.sodalityUser.response;

import java.util.ArrayList;

public class NoticeResponseList {
	private Boolean status;
 	private String message;
 	private ArrayList<NoticeResponseJson> data;
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public ArrayList<NoticeResponseJson> getData() {
		return data;
	}
	public void setData(ArrayList<NoticeResponseJson> data) {
		this.data = data;
	}
 	
 	
}