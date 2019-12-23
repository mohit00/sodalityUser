package com.radius.sodalityUser.repository;

import java.util.ArrayList;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.radius.sodalityUser.model.Complain;
@Repository
public interface ComplainRepository extends UserRepository<Complain, Long> {
    @Query("MATCH(com:Complain)-[rel:UNDER]->(user:user) \r\n" + 
    		"MATCH(com)-[rel2:ASSIGNED_BY]->(user2:user) \r\n" + 
    		"where com.uuid ={uuid}\r\n" + 
    		"return com,rel,rel2,user,user2")
    Complain getComplainByUUId(@Param("uuid") String uuid);

    
    @Query("  Match(com:Complain)-[rel:ASSIGNED_BY]->(user:user)\r\n" + 
    		"    Match(com)-[rel2:ASSIGNED_TO]->(user2:user)\r\n" + 
    		"    where user.uuid ={uuid} AND com.category ={category}\r\n" + 
    		"    return com,user,rel,rel2,user2")
    ArrayList<Complain> getComplainByCategory(@Param("uuid") String uuid,@Param("category") String category);
    @Query("  Match(com:Complain)-[rel:ASSIGNED_BY]->(user:user)\r\n" + 
    		"    Match(com)-[rel2:ASSIGNED_TO]->(user2:user)\r\n" + 
    		"    where user.uuid ={uuid} \r\n" + 
    		"    return com,user,rel,rel2,user2")
    ArrayList<Complain> getComplainList(@Param("uuid") String uuid);
    
}
