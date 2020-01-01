package com.radius.sodalityUser.repository;
import java.util.ArrayList;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.radius.sodalityUser.model.User;

@Repository
public interface UserRepositoryImpl extends UserRepository<User, Long>  { 
	
    @Query("MATCH (u:user ) -[rel:USER_DETAIL]->(de:user_detail)   RETURN u as user,rel,de")
    ArrayList<User> getAllUserWithDetailAdmin();
    @Query("MATCH (u:user ) -[rel:USER_DETAIL]->(de:user_detail)  WHERE ID(u) = {id} RETURN u as user,rel,de ")
    User getAdminDetailWithId(@Param("id") long id);
    @Query("MATCH (u:user ) -[rel:USER_DETAIL]->(de:society_detail) WHERE ID(u) = {id} RETURN u as user,rel,de ")
    User getSocietyDetailWithId(@Param("id") long id);
    @Query("MATCH (u:user ) -[rel:USER_DETAIL]->(de:resident_detail) WHERE ID(u) = {id} RETURN u ,rel,de")
    User getResidentDetailWithId(@Param("id") long id);
    @Query("MATCH (u:user ) -[rel:USER_DETAIL]->(de:staff_detail) WHERE ID(u) = {id} RETURN u as user,rel,de ")
    User getStaffDetailWithId(@Param("id") long id);
    @Query("MATCH (u:user ) WHERE u.email = {email} AND  u.password = {password} RETURN u as user ")
    User loginData(@Param("email") String name,@Param("password") String password);
    @Query("MATCH (u:user )  WHERE ID(u) = {id} RETURN u as user ")
    User getById(@Param("id") long id);
    @Query("MATCH(user0:user) Match (user1) -[rel:UNDER]-> (user0) MATCH (user1:user)-[rel1:USER_DETAIL]->(del:society_detail) where ID(user0) = {id} AND user1.user_type = 'Society' return user1,rel1,del")
    ArrayList<User> getAdminSocietyList(@Param("id") long id);
    
    @Query("MATCH(user0:user) - [rel1:USER_DETAIL]->(del:society_detail) \r\n" + 
    		"OPTIONAL MATCH(del) - [rel2:IMAGE_LIST]->(image:image_List) \r\n" + 
    		"OPTIONAL MATCH(del) - [rel3:AD_LIST]->(adImage:ad_List)   \r\n" + 
    		"with user0,rel1,del,rel2,image,rel3,adImage\r\n" + 
    		" where  user0.uuid ={uuid} return user0,rel1,del,rel2,image,rel3,adImage")
    User getByuuid(@Param("uuid") String uuid);
    @Query("MATCH(user0:user) Match (user0) -[rel:UNDER]-> (user1 :user) MATCH (user0)-[rel1:USER_DETAIL]->(del:resident_detail) MATCH (user0)-[flatOwned:FLAT_OWNED]->(flat:Unit) where user0.uuid = {uuid} AND user0.user_type = 'Resident'  return user0,rel1,del,flat,flatOwned")
    User getResidentDetail(@Param("uuid") String uuid);
    @Query("MATCH(user0:user) Match (user0) -[rel:UNDER]-> (user1 :user) MATCH (user0)-[rel1:USER_DETAIL]->(del:staff_detail)   MATCH(del) -[rel2:CATEGORY]->(cat:Category) where user0.uuid = {uuid} AND user0.user_type = 'Staff'  return user0,rel1,del,rel2,cat")
    User getStaffDetail(@Param("uuid") String uuid);
    
    @Query("Match (user0) -[rel:UNDER]-> (user1 :user) MATCH (user0)-[rel1:USER_DETAIL]->(del:resident_detail)   where user1.uuid = {uuid} AND user0.user_type = 'Resident'  return user0,rel1,del")
    ArrayList<User> getResidentList(@Param("uuid") String uuid);
    
    @Query("Match (user0) -[rel:UNDER]-> (user1 :user) MATCH (user0)-[rel1:USER_DETAIL]->(del:staff_detail) MATCH(del) -[rel2:CATEGORY]->(cat:Category)   where user1.uuid = {uuid} AND user0.user_type = 'Staff'  return user0,rel1,del,rel2,cat")
    ArrayList<User> getStaffList(@Param("uuid") String uuid);
    
    
    @Query("Match (user0) -[rel:UNDER]-> (user1 :user)   MATCH (user0)-[rel1:USER_DETAIL]->(del:Family_Resident)  where user1.uuid = {uuid} AND user1.user_type = 'Resident' AND user0.user_type = 'FamilyMember'  return user0,rel,rel1,del")
    ArrayList<User> getFamilyList(@Param("uuid") String uuid);
    
    @Query("Match (user0:user)  -[rel1:USER_DETAIL]->(del:Family_Resident)  where user0.uuid = {uuid} AND user0.user_type = 'FamilyMember'  return user0,rel1,del")
    User getFamilyByUuid(@Param("uuid") String uuid);
    
    @Query("Match (user0:user)  -[rel1:UNDER]->(del:user)  where user0.uuid = {uuid}   return del")
    User getParentUuid(@Param("uuid") String uuid);
}
