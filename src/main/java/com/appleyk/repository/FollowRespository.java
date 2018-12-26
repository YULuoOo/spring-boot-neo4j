package com.appleyk.repository;


import com.appleyk.model.User;
import com.appleyk.model.Follow;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRespository extends GraphRepository<Follow>{


        //注！在neo4j中 默认id的查询访问为 where ID(anything) =

        //这个是返回所有输入ID的用户关注的人 http://localhost:8080/user/showFollow?firstUserId=473 为测试URL
        @Query("match p=(n:User)-[r:Follow]->(n1:User) where ID(n)={firstUserId}  return n1")
        List<User> findFollows(@Param("firstUserId") Long firstUserId);
        //多加了一个找关注你的人，试了一下应该可以
        @Query("match p=(n:User)-[r:Follow]->(n1:User) where ID(n1)={firstUserId} return n")
        List<User> findFollowers(@Param("firstUserId") Long firstUserId);

        @Query("match p=(n:User)-[r:Follow]->(n1:User) where ID(n)={firstUserId} and ID(n1)={secondUserId} return n1")
        User findFollow(@Param("firstUserId")Long firstUserId ,@Param("secondUserId")Long secondUserId);

        @Query("match p=(n:User)-[r:Follow]->(n1:User) where ID(n)={firstUserId}  delete r")
        void deleteFollows(@Param("firstUserId")Long firstUserId);

        @Query("match p=(n:User)-[r:Follow]->(n1:User) where ID(n)={firstUserId} and ID(n1)={secondUserId} delete r")
        void deleteFollow(@Param("firstUserId")Long firstUserId ,@Param("secondUserId")Long secondUserId);

        @Query("match p=(n:User)-[r:Follow]->(n1:User) where ID(n1)={firstUserId} delete r")
        void deleteFollowers(@Param("firstUserId") Long firstUserId);

        //先使用这个方法，加入关注的关系。如http://localhost:8080/user/follow?firstUserId=66&secondUserId=85
        @Query("MATCH (e:User),(cc:User) \n" +
                "WHERE ID(e) = {firstUserId} AND ID(cc)= {secondUserId} \n" +
                "CREATE (e)-[r:Follow{follow_time:timestamp()}]->(cc) \n" +
                "RETURN r")
        Follow addFollow(@Param("firstUserId") Long firstUserId, @Param("secondUserId") Long secondUserId);


}
