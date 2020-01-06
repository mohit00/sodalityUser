package com.radius.sodalityUser.repository;

 import java.util.ArrayList;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.query.Param;

import com.radius.sodalityUser.model.ComplainComment;
  
public interface ComplainCommentRepository extends UserRepository<ComplainComment, Long> {
	   @Query(" MATCH(comment:ComplainComment) \r\n" + 
	   		" Match (comment) -[rel:UNDER]-> (complain:Complain) \r\n" + 
	   		" Match (comment) -[rel2:COMMENT_BY] -> (user:User) where complain.uuid = {uuid} return comment,rel,rel2,user,complain ")
	    ArrayList<ComplainComment> getAllComment(@Param("uuid") String uuid);

}
