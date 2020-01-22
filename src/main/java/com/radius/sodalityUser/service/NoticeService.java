package com.radius.sodalityUser.service;

import javax.json.JsonObject;

import org.springframework.web.multipart.MultipartFile;

import com.radius.sodalityUser.model.Notice;
import com.radius.sodalityUser.response.NoticeResponseList;

public interface NoticeService {
public Notice addNotice(MultipartFile[] uploadfiles,JsonObject requestBody);
public Notice updateNotice(MultipartFile[] uploadfiles,JsonObject requestBody);
public NoticeResponseList getAllNotice(JsonObject returnJsonObject);
public Notice getNotice(String uuid);
public NoticeResponseList getAllNotification(JsonObject returnJsonObject);
public Notice addDisccusion(MultipartFile[] uploadfiles,JsonObject requestBody);
public Notice updateDisccusion(MultipartFile[] uploadfiles,JsonObject requestBody);
public NoticeResponseList getAllDisccusion(JsonObject returnJsonObject);
public Notice getDisccusionDetail(String uuid);

}
