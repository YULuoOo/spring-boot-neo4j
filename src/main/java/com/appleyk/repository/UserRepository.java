package com.appleyk.repository;


import com.appleyk.model.Return;
import com.appleyk.model.User;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * User 节点增删改
 * @author yukun24@126.com
 * @blob   http://blog.csdn.net/appleyk
 * @date   2018年4月20日13:13:36
 */

@Repository
public interface UserRepository extends GraphRepository<User>{

	/*
	 CoderRepositiory 继承 GraphRepository类，实现增删查改
	   实现自己的接口:通过名字查询Coder(可以是单个Coder，也可以是一个List集合)，
	 spring-data-neo4j 支持方法命名约定查询 findBy{Coder的属性名}，
	 findBy后面的属性名一定要Coder类里存在，否则会报错 
	 */
	@Query("MATCH (User)\n" +
			"WHERE ID(User) = {id}\n" +
			"RETURN User")
	User findById(@Param("id") Long id);

	@Query("create (m:User{username:{username},password:{password}}) RETURN m ")
	User addUserList(@Param("username") String username, @Param("password") String password);

	User findByUsername(@Param("username") String name);

	@Query("match (n:User) RETURN n")
	List<User> showAll();



	@Query("match (n:User{username:{username}}) return ID(n)")
	Long getIDByUsername(@Param("username")String username);

	@Query("match (n:User{username:{username},password:{password}}) RETURN n ")
	User checkLogin(@Param("username") String username, @Param("password")String password);

	@Query("match (n:User) DELETE n")
	void deleteAll();

	@Query("match (User) \n" +
			"where ID(User) = {id}\n" +
			"set User.sex = {sex}\n" +
			"RETURN User")
	User setSex(@Param("id")Long id,@Param("sex") String sex);

	@Query("match (User)\n" +
			"where ID(User) = {id}\n" +
			"set User.address = {address}\n" +
			"RETURN User")
	User setAddress(@Param("id")Long id,@Param("address") String address);

	@Query("match (User)\n" +
			"where ID(User) = {id}\n" +
			"set User.age = {age}\n" +
			"RETURN User")
	User setAge(@Param("id")Long id,@Param("age")String age);

	@Query("match (User)\n" +
			"where ID(User) = {id}\n" +
			"set User.sign = {sign}\n" +
			"RETURN User")
	User setSign(@Param("id")Long id,@Param("sign")String sign);

	@Query("match (User)\n" +
			"where ID(User) = {id}\n" +
			"set User.tags = {tags}\n" +
			"RETURN User")
	User setTags(@Param("id")Long id,@Param("tags")String tags);


//这些都是新加的
	@Query("match (a:User)-[:Follow]->(c)<-[:Follow]-(b:User) where a.username={name1} and b.username={name2}  return c")
	List<User> gongtong(@Param("name1") String name1, @Param("name2")String name2);

	@Query("MATCH (n:User)-[:Follow]->(c)  with n as user,COUNT(*) as count order by count DESC LIMIT 1 return user")
	User guanzhuzuiduo1();
	@Query("MATCH (n:User)-[:Follow]->(c)  with n as user,COUNT(*) as count order by count DESC LIMIT 1 return count")
	int guanzhuzuiduo2();

	@Query("MATCH (n:User)-[:Follow]->(c)  with c as user,COUNT(*) as count order by count DESC LIMIT 1 return user")
	User beiguanzhuzuiduo1();
	@Query("MATCH (n:User)-[:Follow]->(c)  with c as user,COUNT(*) as count order by count DESC LIMIT 1 return count")
	int beiguanzhuzuiduo2();

	@Query("MATCH (n:User)-[:Like]->(c)  with n as user,COUNT(*) as count order by count DESC LIMIT 1 return user")
	User aihaozuiduo1();
	@Query("MATCH (n:User)-[:Like]->(c)  with n as user,COUNT(*) as count order by count DESC LIMIT 1 return count")
	int aihaozuiduo2();

	@Query("match p = shortestPath((a:User{username:{name3}})-[*..15]->(b:User{username:{name4}})) RETURN p")
	List<User> shortest(@Param("name3") String name3, @Param("name4")String name4);
}
