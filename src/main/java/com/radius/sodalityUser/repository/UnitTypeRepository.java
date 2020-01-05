package com.radius.sodalityUser.repository;

import java.util.ArrayList;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.radius.sodalityUser.model.UnitType;
 @Repository
public interface UnitTypeRepository  extends UserRepository<UnitType, Long> {
	 @Query("Match (type:UnitType) -[rel:UNDER]-> (user0:User)" + 
	    		"where user0.uuid = {uuid} AND user0.user_type = 'Society' return type,rel")
	 ArrayList<UnitType>  getAllUnitType(@Param("uuid") String uuid);
	 @Query("Match (type:UnitType) -[rel:UNDER]-> (user0:User)" + 
	    		"where type.uuid = {uuid}  return type,rel")
	 	UnitType  getUnitType(@Param("uuid") String uuid);
}
