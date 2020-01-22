package com.radius.sodalityUser.controller;

import java.util.HashMap;
import java.util.Map;

import javax.json.JsonObject;
import javax.validation.Valid;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
import org.springframework.web.util.HtmlUtils;

import com.radius.sodalityUser.common.Commonfunction;
import com.radius.sodalityUser.model.Greeting;
import com.radius.sodalityUser.model.HelloMessage;
import com.radius.sodalityUser.model.Notice;
import com.radius.sodalityUser.response.NoticeResponseList;
import com.radius.sodalityUser.service.NoticeService;
@RestController
@CrossOrigin
@RequestMapping("/Notice")
public class NoticeController {
	@Autowired
	Commonfunction Commonfunctionl;
	  @Autowired
	  private SimpMessagingTemplate template;

@Autowired 
NoticeService service;
	@PostMapping(value = "/Add/Notice")
	public ResponseEntity<Notice> AddNotice( @RequestParam(value ="files",  required=false) MultipartFile[] uploadfiles ,@RequestParam("data") String data) {
		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(data.toString());
		Notice addNotice = service.addNotice(uploadfiles,returnJsonObject);
		try {
			getAllNotificationb(addNotice.getParrentAccount().getUuid());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ResponseEntity<>(addNotice, HttpStatus.CREATED);
	}
	@PutMapping(value = "/Update/Notice")
	public ResponseEntity<Notice> UpdateNotice( @RequestParam(value ="files",  required=false) MultipartFile[] uploadfiles ,@RequestParam("data") String data) {
		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(data.toString());
		return new ResponseEntity<>(service.updateNotice(uploadfiles,returnJsonObject), HttpStatus.CREATED);
	}
	@PostMapping(value = "/getall/Notice")
	public ResponseEntity<NoticeResponseList> getAllNotice(   @Valid @RequestBody String requestBodyString) {
		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(requestBodyString);
		return new ResponseEntity<>(service.getAllNotice(returnJsonObject), HttpStatus.OK);
	}
	@GetMapping(value = "/get/Notice/{uuid}")
	public ResponseEntity<Notice> getNoticeDetail(@PathVariable("uuid") String uuid) {
 		return new ResponseEntity<>(service.getNotice(uuid), HttpStatus.OK);
	}
	
	@PostMapping (value ="/getall/Notificaiton")
	public ResponseEntity<NoticeResponseList> getAllNotification(   @Valid @RequestBody String requestBodyString) {
		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(requestBodyString);
		return new ResponseEntity<>(service.getAllNotification(returnJsonObject), HttpStatus.OK);
	} 
	
	@PostMapping(value = "/Add/discussion")
	public ResponseEntity<Notice> addDisccusion( @RequestParam(value ="files",  required=false) MultipartFile[] uploadfiles ,@RequestParam("data") String data) {
		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(data.toString());
		Notice addNotice = service.addDisccusion(uploadfiles,returnJsonObject);
		try {
			getAllNotificationb(addNotice.getParrentAccount().getUuid());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ResponseEntity<>(addNotice, HttpStatus.CREATED);
	}
	@PutMapping(value = "/Update/discussion")
	public ResponseEntity<Notice> updateDisccusion( @RequestParam(value ="files",  required=false) MultipartFile[] uploadfiles ,@RequestParam("data") String data) {
		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(data.toString());
		return new ResponseEntity<>(service.updateDisccusion(uploadfiles,returnJsonObject), HttpStatus.CREATED);
	}
	@PostMapping(value = "/getall/discussion")
	public ResponseEntity<NoticeResponseList> getAllDisccusion(   @Valid @RequestBody String requestBodyString) {
		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(requestBodyString);
		return new ResponseEntity<>(service.getAllDisccusion(returnJsonObject), HttpStatus.OK);
	}
	@GetMapping(value = "/get/discussion/{uuid}")
	public ResponseEntity<Notice> getDisccusionDetail(@PathVariable("uuid") String uuid) {
 		return new ResponseEntity<>(service.getDisccusionDetail(uuid), HttpStatus.OK);
	}
	
	 @MessageMapping("/hello/{id}")
 	    public NoticeResponseList getAllNotificationb( @DestinationVariable String id )  throws Exception{
			
 
	      JSONObject obj = new JSONObject();
	      obj.put("parentUUid", id);
 
		 JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(obj.toString());
		    this.template.convertAndSend("/topic/room/"+id, service.getAllNotification(returnJsonObject)  );
		    return service.getAllNotification(returnJsonObject);

 		} 
}
