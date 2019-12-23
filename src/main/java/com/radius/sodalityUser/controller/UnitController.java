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
import com.radius.sodalityUser.model.Unit;
import com.radius.sodalityUser.response.UnitResponseList;
import com.radius.sodalityUser.service.UnitService;

@RestController
@CrossOrigin
@RequestMapping("/Unit")
public class UnitController {
	 @Autowired
	 Commonfunction Commonfunctionl;

	 @Autowired
	 UnitService unitservice;

	 @PostMapping(value ="/Add")
		public ResponseEntity<Unit> AddUnit(@Valid @RequestBody String requestBodyString){
			JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(requestBodyString);
			return new ResponseEntity<>(unitservice.unitSave(returnJsonObject), HttpStatus.OK);
		}

     @PostMapping(value ="/Get")
     public ResponseEntity<UnitResponseList> getUnitTower(@Valid @RequestBody String requestBodyString){
     JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(requestBodyString);
     return new ResponseEntity<>(unitservice.getUnitTower(returnJsonObject), HttpStatus.OK);
     }
     @GetMapping(value ="/Get/{uuid}")
     public ResponseEntity<Unit> getUnit(@PathVariable("uuid") String uuid){
      return new ResponseEntity<>(unitservice.getUnit(uuid), HttpStatus.OK);
     }
	 @PutMapping(value ="/Update")
		public ResponseEntity<Unit> UpdateTower(@Valid @RequestBody String requestBodyString){
			JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(requestBodyString);
			return new ResponseEntity<>(unitservice.unitUpdate(returnJsonObject), HttpStatus.OK);
		}
}
