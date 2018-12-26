package com.appleyk.controller;

import com.appleyk.model.Follow;
import com.appleyk.model.User;

import com.appleyk.model.follower;
import com.appleyk.repository.FollowRespository;
import com.appleyk.repository.RetrieveRespository;
import com.appleyk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
@Controller
@RequestMapping("/search")
public class RetrieveController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RetrieveRespository retrieveRespository;
    @Autowired
    FollowRespository followRespository;
    //模糊查询，搜索用户
    @RequestMapping("/searchname")
    @ResponseBody
    public List<follower> searchName(HttpServletRequest request, HttpSession session){
        String searchName = request.getParameter("name");
        searchName = ".*"+searchName+".*";
        User user = (User)session.getAttribute("user");
        List<User> users = retrieveRespository.searchName(searchName);
        List<follower> result = new ArrayList<follower>();
        for(int i = 0; i < users.size();++i){
            User secUser = users.get(i);
            follower f = new follower();
            f.setUsername(secUser.getUsername());
            f.setNodeId(secUser.getNodeId());
            String relation;
            User tmp =followRespository.findFollow(user.getNodeId(),secUser.getNodeId());
            if(tmp!=null)relation="unfollow";
            else relation="follow";
            f.setRelation(relation);
            result.add(f);
        }
        session.setAttribute("user",user);
        return  result;
    }

}
