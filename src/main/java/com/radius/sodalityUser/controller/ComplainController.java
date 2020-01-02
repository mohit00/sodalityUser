package com.radius.sodalityUser.controller;

import javax.json.JsonObject;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.radius.sodalityUser.common.Commonfunction;
import com.radius.sodalityUser.model.Complain;
import com.radius.sodalityUser.response.ComplainResponseList;
import com.radius.sodalityUser.response.CustomResponse;
import com.radius.sodalityUser.service.ComplainService;

@RestController
@CrossOrigin
@RequestMapping("/Complain")
public class ComplainController {
	@Autowired
	Commonfunction Commonfunctionl;
	@Autowired
	ComplainService service;

	@PostMapping(value = "/Add")
	public ResponseEntity<Complain> AddComplain(
			@RequestParam(value = "files", required = false) MultipartFile[] uploadfiles,
			@RequestParam("data") String data) {
		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(data);
		return new ResponseEntity<>(service.saveComplain(uploadfiles, returnJsonObject), HttpStatus.OK);
	}
	@PostMapping(value = "/Update")
	public ResponseEntity<Complain> updateComplain(
			@RequestParam(value = "files", required = false) MultipartFile[] uploadfiles,
			@RequestParam("data") String data) {
		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(data);
		return new ResponseEntity<>(service.updateComplain(uploadfiles, returnJsonObject), HttpStatus.OK);
	}
	@PostMapping(value="Assign/to")
	public ResponseEntity<Complain> assignToComplain(@Valid @RequestBody String requestBodyString
			 ) {
		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(requestBodyString);
		return new ResponseEntity<>(service.assginTo( returnJsonObject), HttpStatus.OK);
	}
	@PostMapping(value="Status/change")
	public ResponseEntity<CustomResponse> statusChange(@Valid @RequestBody String requestBodyString
			 ) {
		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(requestBodyString);
		return new ResponseEntity<>(service.statusChange( returnJsonObject), HttpStatus.OK);
	}
	@PostMapping(value="By/category")
	public ResponseEntity<ComplainResponseList> getComplainByCategory(@Valid @RequestBody String requestBodyString
			 ) {
		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(requestBodyString);
		return new ResponseEntity<>(service.getComplainByCategory( returnJsonObject), HttpStatus.OK);
	}
	@PostMapping(value="get/List")
	public ResponseEntity<ComplainResponseList> getComplainList(@Valid @RequestBody String requestBodyString
			 ) {
		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(requestBodyString);
		return new ResponseEntity<>(service.getComplainList( returnJsonObject), HttpStatus.OK);
	}
	
	@PostMapping(value="get/Resident/Complain/List")
	public ResponseEntity<ComplainResponseList> getResidentComplainList(@Valid @RequestBody String requestBodyString
			 ) {
		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(requestBodyString);
		return new ResponseEntity<>(service.getResidentComplainList( returnJsonObject), HttpStatus.OK);
	}
	@PostMapping(value="get/Society/Complain/List")
	public ResponseEntity<ComplainResponseList> getSocietyComplainList(@Valid @RequestBody String requestBodyString
			 ) {
		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(requestBodyString);
		return new ResponseEntity<>(service.getSocietyComplainList( returnJsonObject), HttpStatus.OK);
	}
	@GetMapping(value ="get/{uuid}")
	public ResponseEntity<Complain> getComplainDetail(@PathVariable("uuid") String uuid) {
 		return new ResponseEntity<>(service.getComplainDetail( uuid), HttpStatus.OK);
	}
}
