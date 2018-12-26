package com.appleyk.repository;

import com.appleyk.model.User;
import com.appleyk.model.Follow;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RetrieveRespository extends GraphRepository<User>{
    //模糊查询，搜索用户
    @Query("match (n) where n.username =~ {searchName} return n")
    List<User> searchName(@Param("searchName") String searchName);

    @Query("match (n) where n.age ={age} return n")
    List<User> searchAge(@Param("age")String age);
}
