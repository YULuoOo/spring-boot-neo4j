package com.appleyk.repository;


import com.appleyk.model.Like;
import com.appleyk.model.Tag;
import com.appleyk.model.User;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRespository extends GraphRepository<Like>{

        @Query("match p=(n:User)-[r:Like]->(n1:Tag) where n1.name={tagname}  return n")
        List<User> findLikers(@Param("tagname") String tagname);

        @Query("match p=(n:User)-[r:Like]->(n1:Tag) where ID(n)={firstUserId}  return n1")
        List<Tag> findLikes(@Param("firstUserId")Long firstUserId);

        @Query("match p=(n:User)-[r:Like]->(n1:Tag) where ID(n)={firstUserId} and n1.name={tagname} delete r")
        void deleteLike(@Param("firstUserId") Long firstUserId,@Param("tagname")String tagname);

        //先使用这个方法，加入关注的关系。如http://localhost:8080/user/follow?firstUserId=66&secondUserId=85
        @Query("MATCH (e:User),(cc:Tag) \n" +
                "WHERE ID(e) = {firstUserId} AND cc.name= {tagname} \n" +
                "CREATE (e)-[r:Like]->(cc) \n" +
                "RETURN r")
        Like addLike(@Param("firstUserId") Long firstUserId, @Param("tagname")String tagname);


}
