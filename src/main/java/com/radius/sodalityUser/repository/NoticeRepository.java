package com.radius.sodalityUser.repository;

import java.util.ArrayList;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.radius.sodalityUser.model.Notice;

@Repository
public interface NoticeRepository extends UserRepository<Notice, Long> {
	@Query("MATCH (notice:Notice) -[rel:UNDER]->(u:User) where u.uuid = {parentUUid}  and notice.type = 'notice'  RETURN notice,u  ,rel")
	ArrayList<Notice> getAllNotice(@Param("parentUUid") String parentUUid);
	@Query("MATCH (notice:Notice) -[rel:UNDER]->(u:User) where u.uuid = {parentUUid}  and notice.type = 'discussion'  RETURN notice,u  ,rel")
	ArrayList<Notice> getAllDisccusion(@Param("parentUUid") String parentUUid);
	@Query("MATCH (notice:Notice) -[rel:UNDER]->(u:User) where u.uuid = {parentUUid}   and notice.type ={type}  RETURN notice,u  ,rel")
	ArrayList<Notice> getAllNoticeWithType(@Param("parentUUid") String parentUUid, @Param("type") String type);

	@Query("MATCH (notice:Notice) -[rel:UNDER]->(u:User) where notice.uuid = {uuid}     RETURN notice,u  ,rel")
	Notice getNoticeDetail(@Param("uuid") String uuid);

	@Query("MATCH (notice:Notice) -[rel:UNDER]->(u:User)  Match(u)-[rel1:USER_DETAIL] -(del1:society_detail)  where u.uuid = {parentUUid}   OPTIONAL match (notice) -[rel2:DISCCUSION_BY] ->(user2:User)  OPTIONAL match (user2) -[rel3:USER_DETAIL] ->(del2:resident_detail)  RETURN notice,u  ,rel,rel1,del1,rel2,user2,rel3,del2")
	ArrayList<Notice> getAllNotification(@Param("parentUUid") String parentUUid);
}
