package com.radius.sodalityUser.repository;

import java.util.ArrayList;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.radius.sodalityUser.model.notificationComment;

@Repository
public interface NotificationCommentRepository extends UserRepository<notificationComment, Long> {
	@Query("MATCH (not:Notification_Comment) -[rel:UNDER]->(notice:Notice)  \r\n" + 
			" MATCH (not) -[rel2:COMMENT_BY] -(u:User) \r\n" + 
			"  where notice.uuid ={notificationuuid}\r\n" + 
			" OPTIONAL Match (u)-[del1:USER_DETAIL] -(resdel:resident_detail)\r\n" + 
			"  OPTIONAL Match (u)-[del2:USER_DETAIL] -(staf:staff_detail)\r\n" + 
			"  OPTIONAL Match (u)-[del3:USER_DETAIL] -(society:society_detail)\r\n" + 
			" return not,rel,notice,rel2,u,del1,del2,del3,resdel,staf,society")
	ArrayList<notificationComment> getNoticeCommentList(@Param("notificationuuid") String notificationuuid);

}
