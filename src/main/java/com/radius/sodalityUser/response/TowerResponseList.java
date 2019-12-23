package com.radius.sodalityUser.response;

import java.util.ArrayList;

public class TowerResponseList {
	private Boolean status;
 	private ArrayList<TowerResponsejson> data;
	private String message;
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public ArrayList<TowerResponsejson> getData() {
		return data;
	}
	public void setData(ArrayList<TowerResponsejson> data) {
		this.data = data;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
