package com.radius.sodalityUser.service;

import javax.json.JsonObject;

import org.springframework.web.multipart.MultipartFile;

import com.radius.sodalityUser.model.Complain;
import com.radius.sodalityUser.response.ComplainResponseList;
import com.radius.sodalityUser.response.CustomResponse;

public interface ComplainService {
public Complain saveComplain(MultipartFile[] uploadfiles,JsonObject requestBody);
public Complain updateComplain(MultipartFile[] uploadfiles,JsonObject requestBody);

public Complain assginTo(JsonObject requestBody);
public CustomResponse statusChange(JsonObject requestBody);
public ComplainResponseList getComplainByCategory(JsonObject requestBody);
public ComplainResponseList getComplainList(JsonObject requestBody);
public ComplainResponseList getResidentComplainList(JsonObject requestBody);
public ComplainResponseList getSocietyComplainList(JsonObject requestBody);

public Complain getComplainDetail(String uuid);
}
