package com.radius.sodalityUser.controller;

import java.util.ArrayList;
import java.util.List;

import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.radius.sodalityUser.common.Commonfunction;
import com.radius.sodalityUser.model.User;
import com.radius.sodalityUser.response.FamilyListResponse;
import com.radius.sodalityUser.response.ParentGetJson;
import com.radius.sodalityUser.response.ResidentListResponse;
import com.radius.sodalityUser.response.SocietyListResponse;
import com.radius.sodalityUser.response.StaffListResponse;
import com.radius.sodalityUser.response.UserResponse;
import com.radius.sodalityUser.service.TowerService;
import com.radius.sodalityUser.service.UserService;
 
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserService service;
	 
	@Autowired
	TowerService tService;
 @Autowired
 Commonfunction Commonfunctionl;
	@PostMapping(value = "/create")
	public ResponseEntity<User> usercreate(@Valid @RequestBody String requestBodyString) {
         		
		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(requestBodyString);
		User user = service.saveUser(returnJsonObject);
	    return new ResponseEntity<>(user, HttpStatus.CREATED);
	}
	@GetMapping(value ="/get/allAdmin")
	public ResponseEntity<List<User>> userGet(){
		List<User> userList = service.getuser();
	    return new ResponseEntity<>(userList, HttpStatus.OK);

	}
	@PostMapping(value ="/getbyId/{userId}")
	public ResponseEntity<User> userGetById(@PathVariable("userId") long id,@Valid @RequestBody String requestBodyString){
		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(requestBodyString);
		User user = service.getuserById(returnJsonObject,id);
	    return new ResponseEntity<>(user, HttpStatus.OK);
	}
	@PostMapping(value ="/login")
	public ResponseEntity<UserResponse> login(@Valid @RequestBody String requestBodyString){
		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(requestBodyString);
	    return new ResponseEntity<>(service.login(returnJsonObject), HttpStatus.OK);

	}
	@PostMapping(value ="/update")
	public ResponseEntity<User> userupdate(@Valid @RequestBody String requestBodyString) {		
		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(requestBodyString);
		User user = service.updateUser(returnJsonObject);
	    return new ResponseEntity<>(user, HttpStatus.CREATED);
	}
	@PostMapping(value ="/get/admin/society")
	public ResponseEntity<SocietyListResponse> getSociety(@Valid @RequestBody String requestBodyString) {		
		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(requestBodyString);
		SocietyListResponse society = service.getSociety(returnJsonObject);
	    return new ResponseEntity<>(society, HttpStatus.CREATED);
	}
	
	// Group Create 
	@PostMapping(value ="/Save/Group")
	public  ResponseEntity<User> saveGroup( @RequestParam(value ="files",  required=false) MultipartFile uploadfiles,   @RequestParam("data") String data) {
	 
		System.out.println(data);

		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(data.toString());
  		
		return new ResponseEntity<>(service.saveGroup(uploadfiles,returnJsonObject), HttpStatus.OK);
	}
	@PostMapping(value ="/get/Group")
	public  ResponseEntity<ArrayList<User>> getGroup(@Valid @RequestBody String requestBodyString) {
		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(requestBodyString);

		return new ResponseEntity<>(service.getGroup(returnJsonObject), HttpStatus.OK);
	}
	@GetMapping(value ="/get/Group/{uuid}")
	public  ResponseEntity<User> getGroupByuuid(@PathVariable("uuid") String uuid) {
		System.out.println(uuid);
		return new ResponseEntity<>(service.getGroupByuuId(uuid), HttpStatus.OK);
	}
	@PutMapping(value ="/Group/Update")
	public ResponseEntity<User> updateGroup( @RequestParam(value ="files",  required=false) MultipartFile uploadfiles,   @RequestParam("data") String data){
		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(data);
		User u = service.updateGroup(uploadfiles,returnJsonObject);
		return new ResponseEntity<>(u, HttpStatus.OK);
	}
	
	@PostMapping(value ="/Save/society")
	public  ResponseEntity<User> saveSociety( @RequestParam(value ="files",  required=false) MultipartFile[] uploadfiles,HttpServletRequest request,@RequestParam(value="adImage",required=false) MultipartFile[]  adImage,@RequestParam(value="billLogo",required=false) MultipartFile billLogo,@RequestParam(value="societyLogo",required=false) MultipartFile societyLogo,@RequestParam("data") String data) {
	 
		System.out.println(data);

		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(data.toString());
  		
		return new ResponseEntity<>(service.saveSociety(uploadfiles,request,adImage,billLogo,societyLogo,returnJsonObject), HttpStatus.OK);
	}
	@GetMapping(value ="/get/society/{uuid}")
	public  ResponseEntity<User> getSocietyByuuid(@PathVariable("uuid") String uuid) {
		return new ResponseEntity<>(service.getSocietyByuuId(uuid), HttpStatus.OK);
	}
	@PutMapping(value ="/Society/Update")
	public ResponseEntity<User> updateSociety(@RequestParam(value ="files",  required=false) MultipartFile[] uploadfiles,HttpServletRequest request,@RequestParam(value="adImage",required=false) MultipartFile[]  adImage,@RequestParam(value="billLogo",required=false) MultipartFile billLogo,@RequestParam(value="societyLogo",required=false) MultipartFile societyLogo,@RequestParam("data") String data){
		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(data);
		User u = service.updateSociety(uploadfiles,request,adImage,billLogo,societyLogo,returnJsonObject);
		return new ResponseEntity<>(u, HttpStatus.OK);
	}
	
	@PostMapping(value ="/resident/save")
	public  ResponseEntity<User> saveresident( @RequestParam(value ="files",  required=false) MultipartFile uploadfiles,@RequestParam("data") String data) {
	 
		System.out.println(data);

		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(data.toString());
  		
		return new ResponseEntity<>(service.saveResident(uploadfiles,returnJsonObject), HttpStatus.OK);
	}
	@PutMapping(value ="/resident/update")
	public  ResponseEntity<User> updateResident( @RequestParam(value ="files",  required=false) MultipartFile uploadfiles,@RequestParam("data") String data) {
	 
		System.out.println(data);

		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(data.toString());
  		
		return new ResponseEntity<>(service.updateResident(uploadfiles,returnJsonObject), HttpStatus.OK);
	}
	@GetMapping(value ="/get/resident/{uuid}")
	public  ResponseEntity<User> getResident(@PathVariable("uuid") String uuid){
		return new ResponseEntity<>(service.getResidentUuid(uuid), HttpStatus.OK);
	}
	@PostMapping(value ="/get/society/resident")
	public ResponseEntity<ResidentListResponse> getResidentList(@Valid @RequestBody String requestBodyString) {		
		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(requestBodyString);
		ResidentListResponse society = service.getResidentList(returnJsonObject);
	    return new ResponseEntity<>(society, HttpStatus.OK);
	}
	
	@PostMapping(value ="/staff/save")
	public  ResponseEntity<User> savestaff( @RequestParam(value ="files",  required=false) MultipartFile uploadfiles,@RequestParam("data") String data) {
	 
		System.out.println(data);

		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(data.toString());
  		
		return new ResponseEntity<>(service.saveStaff(uploadfiles,returnJsonObject), HttpStatus.OK);
	}
	@PutMapping(value ="/staff/update")
	public  ResponseEntity<User> updateStaff( @RequestParam(value ="files",  required=false) MultipartFile uploadfiles,@RequestParam("data") String data) {
	 
		System.out.println(data);

		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(data.toString());
  		
		return new ResponseEntity<>(service.updateStaff(uploadfiles,returnJsonObject), HttpStatus.OK);
	}
	@GetMapping(value ="/get/staff/{uuid}")
	public  ResponseEntity<User> getStaff(@PathVariable("uuid") String uuid){
		return new ResponseEntity<>(service.getStaffuuid(uuid), HttpStatus.OK);
	}
	@PostMapping(value ="/get/society/staff")
	public ResponseEntity<StaffListResponse> getStaffList(@Valid @RequestBody String requestBodyString) {		
		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(requestBodyString);
		StaffListResponse society = service.getStaffList(returnJsonObject);
	    return new ResponseEntity<>(society, HttpStatus.OK);
	}
	
	@PostMapping(value ="/get/society/staff/byCategory")
	public ResponseEntity<StaffListResponse> getStaffListByCategory(@Valid @RequestBody String requestBodyString) {		
		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(requestBodyString);
		StaffListResponse society = service.getStaffListByCategory(returnJsonObject);
	    return new ResponseEntity<>(society, HttpStatus.OK);
	}
	@PostMapping(value ="/family/save")
	public  ResponseEntity<User> savefamily( @RequestParam(value ="files",  required=false) MultipartFile uploadfiles,@RequestParam("data") String data) {
	 
		System.out.println(data);

		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(data.toString());
  		
		return new ResponseEntity<>(service.saveFamilyMember(uploadfiles,returnJsonObject), HttpStatus.OK);
	}
	@PutMapping(value ="/family/update")
	public  ResponseEntity<User> updateFamily( @RequestParam(value ="files",  required=false) MultipartFile uploadfiles,@RequestParam("data") String data) {
		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(data.toString());
		return new ResponseEntity<>(service.updateFamilyMember(uploadfiles,returnJsonObject), HttpStatus.OK);
	}
	@PostMapping(value ="/family/get")
	public  ResponseEntity<FamilyListResponse> updateFamily(@Valid @RequestBody String requestBodyString) {
	 
 
		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(requestBodyString);
  		
		return new ResponseEntity<>(service.getFamilyList(returnJsonObject), HttpStatus.OK);
	}
	@GetMapping(value ="/family/get/{uuid}")
	public  ResponseEntity<User> getFamilyDetail(@PathVariable("uuid") String uuid) {
	 
 
   		
		return new ResponseEntity<>(service.getFamilyDetail(uuid), HttpStatus.OK);
	}
	@GetMapping(value ="get/parent/uuid/{uuid}")
	public ResponseEntity<ParentGetJson> getparentUUid(@PathVariable("uuid") String uuid) {
		
		return new ResponseEntity<>(service.getParentDetail(uuid), HttpStatus.OK);
 
	}
}

 

 
