package com.radius.sodalityUser.service;

import java.util.ArrayList;

import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.radius.sodalityUser.model.User;
import com.radius.sodalityUser.response.UserResponse;
import com.radius.sodalityUser.response.FamilyListResponse;
import com.radius.sodalityUser.response.ParentGetJson;
import com.radius.sodalityUser.response.ResidentListResponse;
import com.radius.sodalityUser.response.SocietyListResponse;
import com.radius.sodalityUser.response.StaffListResponse;

public interface UserService {

	public User saveUser(JsonObject requestBody);

	public User updateUser(JsonObject requestBody);

	public SocietyListResponse getSociety(JsonObject requestBody);

	public User getSocietyByuuId(String uuid);

	public User updateSociety(MultipartFile[] uploadfiles, HttpServletRequest request, MultipartFile[] adImage,
			MultipartFile billLogo, MultipartFile societyLogo, JsonObject requestBody);

	public ArrayList<User> getuser();

	public User getuserById(JsonObject requestBody, long id);

	public UserResponse login(JsonObject requestBody);

	public User saveSociety(MultipartFile[] uploadfiles, HttpServletRequest request, MultipartFile[] adImage,
			MultipartFile billLogo, MultipartFile societyLogo, JsonObject requestBody);

	public User saveResident(MultipartFile uploadfiles, JsonObject requestBody);

	public User updateResident(MultipartFile uploadfiles, JsonObject requestBody);

	public User getResidentUuid(String uuid);

	public ResidentListResponse getResidentList(JsonObject requestBody);

	public User saveStaff(MultipartFile uploadfiles, JsonObject requestBody);

	public User updateStaff(MultipartFile uploadfiles, JsonObject requestBody);

	public StaffListResponse getStaffList(JsonObject requestBody);

	public User getStaffuuid(String uuid);

	public User saveFamilyMember(MultipartFile uploadfiles, JsonObject requestBody);
	public User updateFamilyMember(MultipartFile uploadfiles, JsonObject requestBody);
	public FamilyListResponse getFamilyList( JsonObject requestBody);
	public User getFamilyDetail(String uuid);
	public ParentGetJson getParentDetail(String uuid);
	


}
