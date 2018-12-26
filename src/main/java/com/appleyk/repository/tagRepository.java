package com.appleyk.repository;


import com.appleyk.model.Tag;
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
public interface tagRepository extends GraphRepository<Tag>{

	/*
	 CoderRepositiory 继承 GraphRepository类，实现增删查改
	   实现自己的接口:通过名字查询Coder(可以是单个Coder，也可以是一个List集合)，
	 spring-data-neo4j 支持方法命名约定查询 findBy{Coder的属性名}，
	 findBy后面的属性名一定要Coder类里存在，否则会报错
	 */
	@Query("match (n:Tag) return n")
	public List<Tag> showAll();

	@Query("create (n:Tag{name:{name}}) return n")
	public Tag addTag(@Param("name")String name);

	@Query("match p=(n:Tag) where ID(n)={tagId}  delete n")
    public Tag deleteTag(@Param("tagId")Long id);

	@Query("match (n:Tag{name:{name}}) return n")
	public Tag findTagByName(@Param("name")String name);


}
