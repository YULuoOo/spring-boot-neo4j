package com.appleyk.model;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@RelationshipEntity(type = "Follow")
public class Follow {
    @GraphId
    private Long id;

    @StartNode
    private User startNode;

    @EndNode
    private User endNode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getStartNode() {
        return startNode;
    }

    public void setStartNode(User startNode) {
        this.startNode = startNode;
    }

    public User getEndNode() {
        return endNode;
    }

    public void setEndNode(User endNode) {
        this.endNode = endNode;
    }
}