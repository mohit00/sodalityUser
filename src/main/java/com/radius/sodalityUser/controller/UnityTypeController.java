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
import com.radius.sodalityUser.model.UnitType;
import com.radius.sodalityUser.response.UnitTypeResponseList;
import com.radius.sodalityUser.service.UnitTypeService;
 
@RestController
@CrossOrigin
@RequestMapping("/Unittype")
public class UnityTypeController {
	 @Autowired
	 Commonfunction Commonfunctionl;

	 @Autowired
	 UnitTypeService unitTypeServices;
	
	 @PostMapping(value ="/Add")
		public ResponseEntity<UnitType> AddUnit(@Valid @RequestBody String requestBodyString){
			JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(requestBodyString);
			return new ResponseEntity<>(unitTypeServices.unitSave(returnJsonObject), HttpStatus.CREATED);
		}

    @PostMapping(value ="/Get")
    public ResponseEntity<UnitTypeResponseList> getUnitTower(@Valid @RequestBody String requestBodyString){
    JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(requestBodyString);
    return new ResponseEntity<>(unitTypeServices.getUnitTypeList(returnJsonObject), HttpStatus.OK);
    }
    @GetMapping(value ="/Get/{uuid}")
    public ResponseEntity<UnitType> getUnit(@PathVariable("uuid") String uuid){
     return new ResponseEntity<>(unitTypeServices.getUnitType(uuid), HttpStatus.OK);
    }
	 @PutMapping(value ="/Update")
		public ResponseEntity<UnitType> UpdateTower(@Valid @RequestBody String requestBodyString){
			JsonObject returnJsonObject = Commonfunctionl.ReturnJsonObject(requestBodyString);
			return new ResponseEntity<>(unitTypeServices.unitSave(returnJsonObject), HttpStatus.CREATED);
		}
}
