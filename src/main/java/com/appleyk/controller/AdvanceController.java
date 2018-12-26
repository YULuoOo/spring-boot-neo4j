package com.appleyk.controller;
import com.appleyk.MySessionContext;
import com.appleyk.model.*;
import com.appleyk.repository.LikeRespository;
import com.appleyk.repository.tagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.appleyk.repository.UserRepository;
import com.appleyk.repository.FollowRespository;
import com.appleyk.result.ResponseResult;
import org.springframework.web.servlet.ModelAndView;
import sun.rmi.runtime.Log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
@RequestMapping("/advance") //restful风格的api接口


public class AdvanceController {
    @Autowired
    LikeRespository likeRespository;
    @Autowired
    tagRepository tagRepository;
    @Autowired
    UserRepository userRepositiory;
    @Autowired
    FollowRespository followRespositiory;
    private Long userID;
    private User user = new User();

    @RequestMapping("/gongtong")
    @ResponseBody
    public List<User> gongtong(HttpServletRequest request, HttpSession session) {
        String name1 = request.getParameter("name1");
        String name2 = request.getParameter("name2");

        List<User> list = userRepositiory.gongtong(name1,name2);
        return list;
    }
    class data
    {
       public User user;
       public int count;
    }

    @RequestMapping("/guanzhuzuiduo")
    @ResponseBody
    public Return guanzhuzuiduo(HttpServletRequest request, HttpSession session) {
        Return user = new Return();
        user.user = userRepositiory.guanzhuzuiduo1();
        user.count = userRepositiory.guanzhuzuiduo2();
        return user;
    }

    @RequestMapping("/beiguanzhuzuiduo")
    @ResponseBody
    public Return beiguanzhuzuiduo(HttpServletRequest request, HttpSession session) {
        Return user = new Return();
        user.user = userRepositiory.beiguanzhuzuiduo1();
        user.count = userRepositiory.beiguanzhuzuiduo2();
        return user;
    }
    @RequestMapping("/aihaozuiduo")
    @ResponseBody
    public Return aihaozuiduo(HttpServletRequest request, HttpSession session) {
        Return user = new Return();
        user.user = userRepositiory.aihaozuiduo1();
        user.count = userRepositiory.aihaozuiduo2();
        return user;
    }

    @RequestMapping("/shortest")
    @ResponseBody
    public List<User> shortest(HttpServletRequest request, HttpSession session) {
        String name3 = request.getParameter("name3");
        String name4 = request.getParameter("name4");

        List<User> list = userRepositiory.shortest(name3,name4);
        return list;
    }
}
