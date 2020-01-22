package com.radius.sodalityUser.controller;

import java.util.ArrayList;

import javax.json.JsonObject;
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
import com.radius.sodalityUser.model.Notice;
import com.radius.sodalityUser.model.notificationComment;
import com.radius.sodalityUser.response.NoticeResponseList;
import com.radius.sodalityUser.service.NotificationCommentService;

@RestController
@CrossOrigin
@RequestMapping("/Notication")
public class NotificationCommentController {
	@Autowired
	Commonfunction Commonfunctionl;
@Autowired
NotificationCommentService service ;
	@PostMapping(value = "/Add/comment")
	public ResponseEntity<notificationComment> addComment( @Valid @RequestBody String requestBodyString  ) {
		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(requestBodyString);
		 
		return new ResponseEntity<>(service.addComment(returnJsonObject), HttpStatus.CREATED);
	}
	@PutMapping(value = "/Update/comment")
	public ResponseEntity<notificationComment> UpdateComment(@Valid @RequestBody String requestBodyString) {
		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(requestBodyString);
		return new ResponseEntity<>(service.UpdateComment(returnJsonObject), HttpStatus.CREATED);
	}
	@PostMapping(value = "/getall/comment")
	public ResponseEntity<ArrayList<notificationComment>> getAllNotice(@Valid @RequestBody String requestBodyString) {
		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(requestBodyString);
		return new ResponseEntity<>(service.getAllComment(returnJsonObject), HttpStatus.OK);
	}
	@GetMapping(value = "/get/comment/{uuid}")
	public ResponseEntity<notificationComment> getNoticeDetail(@PathVariable("uuid") String uuid) {
 		return new ResponseEntity<>(service.getComment(uuid), HttpStatus.OK);
	}
	
}
