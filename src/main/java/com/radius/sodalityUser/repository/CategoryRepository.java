package com.radius.sodalityUser.repository;

import java.util.ArrayList;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.radius.sodalityUser.model.Category;
@Repository
public interface CategoryRepository extends UserRepository<Category, Long> {

    @Query("MATCH(cat:Category)-[rel:UNDER]->(user:User) WHERE ID(user) ={id} AND user.user_type='Society'       return cat")
    ArrayList<Category> getAllCategory(@Param("id") Long id);
    @Query("Match (cat:Category) where cat.uuid = {uuid}   return cat")
    Category getCategoryDetail(@Param("uuid") String uuid);
    
    
}
