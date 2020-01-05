package com.radius.sodalityUser.response;

import java.util.ArrayList;

public class ComplainCommentResponseList {
	private Boolean status;
 	private String message;
 	private ArrayList<ComplainCommentResponseJson> data;
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
	public ArrayList<ComplainCommentResponseJson> getData() {
		return data;
	}
	public void setData(ArrayList<ComplainCommentResponseJson> data) {
		this.data = data;
	}
 	
}
