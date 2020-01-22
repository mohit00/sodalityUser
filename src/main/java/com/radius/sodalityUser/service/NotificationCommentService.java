package com.radius.sodalityUser.service;

import java.util.ArrayList;

import javax.json.JsonObject;

import com.radius.sodalityUser.model.notificationComment;

public interface NotificationCommentService {
public notificationComment addComment(JsonObject requestBody);
public notificationComment UpdateComment(JsonObject requestBody);
public ArrayList<notificationComment> getAllComment(JsonObject requestBody);
public notificationComment getComment(String uuid);
}
