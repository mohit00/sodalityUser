package com.radius.sodalityUser.service;

import javax.json.JsonObject;

import com.radius.sodalityUser.model.ComplainComment;
import com.radius.sodalityUser.response.ComplainCommentResponseList;

public interface ComplainCommentService {
public ComplainComment addComplainComment(JsonObject requestBody);
public ComplainCommentResponseList getCommentList(JsonObject requestBody);
}
