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
    @Query("MATCH(com:Complain)  \r\n" + 
    		" where  com.uuid ={uuid}\r\n" + 
    		" set com.complainStatus ={status}\r\n" + 
    		"\r\n" + 
    		" return com")
    Complain changeStatus(@Param("uuid") String uuid,@Param("status") String status);

    
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
    @Query("  Match(com:Complain)-[rel:UNDER]->(user:user) \r\n" + 
    		"Match(com) -[:CATEGORY]->(cat:Category)\r\n" + 
    		"    	 where user.uuid ={uuid} \r\n" + 
    		"    	  return com,cat,rel")
    ArrayList<Complain> getcomplainResponseList(@Param("uuid") String uuid);
    
    @Query("  Match(com:Complain)-[rel:ASSIGNED_BY]->(user:user) \r\n" + 
    		"Match(com) -[:CATEGORY]->(cat:Category)\r\n" + 
    		"    	 where user.uuid ={uuid} \r\n" + 
    		"    	  return com,cat,rel")
    ArrayList<Complain> getSocietyComplainResponseList(@Param("uuid") String uuid);
    @Query(" Match(com:Complain)-[rel:ASSIGNED_TO]->(user:user) \r\n" + 
    		"    		 Match(com) -[:CATEGORY]->(cat:Category) \r\n" + 
    		"    	  where user.uuid ={uuid}\r\n" + 
    		"     	return com,cat,rel")
    ArrayList<Complain> getStaffComplainResponseList(@Param("uuid") String uuid);
    @Query("Match(com:Complain)\r\n" + 
    		"Match(com)-[rel:UNDER]->(user:user)\r\n" + 
    		"Match(com) -[rel2:CATEGORY]->(cat:Category)\r\n" + 
    		"Match(com)-[rel3:COMPLAIN_ON] ->(unit:Unit)\r\n" + 
    		"Match(com)-[rel4:ASSIGNED_BY]->(user1:user)\r\n" + 
    		"\r\n" + 
    		"where com.uuid ={uuid}\r\n" + 
    		"OPTIONAL  Match(com) -[rel5:ASSIGNED_TO]->(user2:user)\r\n" + 
    		"OPTIONAL  Match(com) -[rel6:COMPLAIN_COMENTS]->(user3:user)\r\n" + 
    		"return com,cat,rel,user,rel2,rel3,unit,rel4,user1,rel5,user2,rel6,user3")
    Complain getComplainDetail(@Param("uuid") String uuid);
    
}
