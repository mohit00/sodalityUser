package com.radius.sodalityUser.repository;

import java.util.ArrayList;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.radius.sodalityUser.model.Tower;
 @Repository
public interface TowerRepository  extends UserRepository<Tower, Long>  { 

	    @Query("MATCH(user0:User)\r\n" + 
	    		"Match (tower:Tower) -[rel:UNDER]-> (user0) MATCH (user1:User)\r\n" + 
	    		"where user0.uuid = {uuid} AND user0.user_type = 'Society' return tower,rel")
	    ArrayList<Tower> getAllTower(@Param("uuid") String uuid);
	    @Query("Match (tower:Tower) where tower.uuid = {uuid}   return tower")
	    Tower gettower(@Param("uuid") String uuid);
}
