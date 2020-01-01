package com.radius.sodalityUser.repository;
import java.util.ArrayList;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.radius.sodalityUser.model.Unit;
 @Repository
public interface UnitRepository  extends UserRepository<Unit, Long>  {
	    @Query("Match (unit:Unit) -[rel:UNDER]-> (tower:Tower) where tower.uuid = {uuid}  RETURN unit")
	    ArrayList<Unit> getAllUnit(@Param("uuid") String uuid);
	    @Query(" Match (unit:Unit)  where unit.uuid = {uuid}  RETURN unit")
	    Unit getUnit(@Param("uuid") String uuid);
	    @Query("Match (unit:Unit) <-[rel:FLAT_OWNED]- (u:user)  where u.uuid = {uuid}  RETURN unit")
	    ArrayList<Unit> getResidentUnitList(@Param("uuid") String uuid);
}