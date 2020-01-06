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
import org.springframework.web.bind.annotation.RestController;

import com.radius.sodalityUser.common.Commonfunction;
import com.radius.sodalityUser.model.Category;
import com.radius.sodalityUser.model.Tower;
import com.radius.sodalityUser.service.CategoryService;

@RestController
@CrossOrigin
@RequestMapping("/Category")
public class CategoryController {
	@Autowired
	CategoryService service;
	@Autowired
	Commonfunction Commonfunctionl;

	@PostMapping(value = "/Add")
	public ResponseEntity<Category> AddComplain( @Valid @RequestBody String requestBodyString) {
		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(requestBodyString);
		return new ResponseEntity<>(service.saveCategory( returnJsonObject), HttpStatus.OK);
	}
	@PostMapping(value = "/Get")
	public ResponseEntity<ArrayList<Category>> getComplain( @Valid @RequestBody String requestBodyString) {
		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(requestBodyString);
		return new ResponseEntity<>(service.getCategory( returnJsonObject), HttpStatus.OK);
	}
	@PostMapping(value = "/Get/ByResident")
	public ResponseEntity<ArrayList<Category>> getCategoryByResidentId( @Valid @RequestBody String requestBodyString) {
		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(requestBodyString);
		return new ResponseEntity<>(service.getCategoryByResidentIId( returnJsonObject), HttpStatus.OK);
	}
	@GetMapping(value = "/Get/{uuid}")
	public ResponseEntity<Category> getCategoryDetail(@PathVariable("uuid") String uuid) {
 		return new ResponseEntity<>(service.getCategoryDetail(uuid), HttpStatus.OK);
	}
	@PutMapping(value ="Update")
	public ResponseEntity<Category> categoryUpdate(@Valid @RequestBody String requestBodyString){
		JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(requestBodyString);
		return new ResponseEntity<>(service.categoryUpdate(returnJsonObject), HttpStatus.OK);
	}
}
