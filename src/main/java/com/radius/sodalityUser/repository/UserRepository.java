package com.radius.sodalityUser.repository;
import java.io.Serializable;

import org.springframework.data.neo4j.repository.Neo4jRepository;
public interface UserRepository<T, R extends Serializable> extends Neo4jRepository<T, R> {

}