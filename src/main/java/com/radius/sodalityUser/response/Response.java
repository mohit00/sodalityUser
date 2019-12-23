package com.radius.sodalityUser.response;

import org.springframework.data.neo4j.annotation.QueryResult;

import com.radius.sodalityUser.model.User;
import com.radius.sodalityUser.model.UserDetail;
 @QueryResult
public class Response {
	 private   User user;
	 private  UserDetail userDetail;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public UserDetail getUserDetail() {
		return userDetail;
	}
	public void setUserDetail(UserDetail userDetail) {
		this.userDetail = userDetail;
	}

}
