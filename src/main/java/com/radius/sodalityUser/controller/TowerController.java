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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.radius.sodalityUser.common.Commonfunction;
import com.radius.sodalityUser.model.Tower;
import com.radius.sodalityUser.response.TowerResponseList;
import com.radius.sodalityUser.service.TowerService;
import com.radius.sodalityUser.service.UserService;
@RestController
@CrossOrigin
@RequestMapping("/Tower")
public class TowerController {
	@Autowired
	UserService service;
	 
	@Autowired
	TowerService tService;
 @Autowired
 Commonfunction Commonfunctionl;
 @PostMapping(value ="/Add")
	public ResponseEntity<Tower> AddTower(@Valid @RequestBody String requestBodyString){
		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(requestBodyString);
		return new ResponseEntity<>(tService.saveTower(returnJsonObject), HttpStatus.OK);
	}
	@PostMapping(value ="get/List")
	public ResponseEntity<TowerResponseList> getTowerList(@Valid @RequestBody String requestBodyString){
		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(requestBodyString);
		return new ResponseEntity<>(tService.getTowerList(returnJsonObject), HttpStatus.OK);
	}
	@GetMapping(value ="get/{uuid}")
	public ResponseEntity<Tower> getTower(@PathVariable("uuid") String uuid){
		 
 		return new ResponseEntity<>(tService.getTower(uuid), HttpStatus.OK);
	}
	@PutMapping(value ="Update")
	public ResponseEntity<Tower> updateTower(@Valid @RequestBody String requestBodyString){
		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(requestBodyString);
		return new ResponseEntity<>(tService.updateTower(returnJsonObject), HttpStatus.OK);
	}
	
}
