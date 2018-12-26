package com.appleyk.controller;

import com.appleyk.MySessionContext;
import com.appleyk.model.Tag;
import com.appleyk.model.User;
import com.appleyk.model.Follow;
import com.appleyk.model.follower;
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
@RequestMapping("/user") //restful风格的api接口
public class UserController {

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
    private MySessionContext myc = MySessionContext.getInstance();


    @RequestMapping(value = "/userLogin")
    @ResponseBody
    public ModelAndView userLogin(HttpServletRequest request, HttpSession session) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        user = userRepositiory.checkLogin(username, password);
        if (user != null) {
            userID = userRepositiory.getIDByUsername(username);
            List<User> follows = followRespositiory.findFollows(userID);
            List<User> followers = followRespositiory.findFollowers(userID);
            ModelAndView mav = new ModelAndView("personalpage");
            session.setAttribute("user", user);
            session.setAttribute("follows", follows);
            session.setAttribute("followers", followers);
            myc.addSession(session);
            return mav;
        } else {
            ModelAndView mav = new ModelAndView("login");
            return mav;
        }
    }

    @RequestMapping("/userRegister")
    @ResponseBody
    public User userRegister(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (username != null && password != null) {
            user = userRepositiory.findByUsername(username);
            if (user == null) {
                user = userRepositiory.addUserList(username, password);
            }
        }
        return user;
    }


    @RequestMapping("/changeInfo")
    @ResponseBody
    public ModelAndView changeInfo(HttpServletRequest request, HttpSession session) {
        String sex = request.getParameter("sex");
        String address = request.getParameter("address");
        String sign = request.getParameter("sign");
        String age = request.getParameter("age");
        //HttpSession session = myc.getSession(sessionId);
        //User user = (User)session.getAttribute("user");
        userRepositiory.setAge(user.getNodeId(), age);
        userRepositiory.setSex(user.getNodeId(), sex);
        userRepositiory.setAddress(user.getNodeId(), address);
        userRepositiory.setSign(user.getNodeId(), sign);
        user = userRepositiory.findById(userID);
        session.setAttribute("user", user);
        myc.addSession(session);

        //session.setAttribute("user",userRepositiory.findById(user.getNodeId()));
        return new ModelAndView("redirect:/user/userLogin?username=" + user.getUsername() + "&password=" + user.getPassword());
    }

    @RequestMapping("/findByName")
    @ResponseBody
    public User findById(@RequestParam(value = "name") String name) {
        return userRepositiory.findByUsername(name);
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView find(@RequestParam(value = "name") String name) {
        User mylist = userRepositiory.findByUsername(name);
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("user", mylist);
        return mav;
    }

    @RequestMapping(value = "/show", method = RequestMethod.GET)
    @ResponseBody
    public List<User> showAll() {
        List<User> mylist = userRepositiory.showAll();
        return mylist;
    }

    @PostMapping("/save")
    @ResponseBody
    @Transactional
    public ResponseResult Create(@RequestBody User coder) throws Exception {

        User result = userRepositiory.save(coder);
        if (result != null) {
            return new ResponseResult(200, result.getUsername() + "节点创建成功");
        }
        return new ResponseResult(500, coder.getUsername() + "节点创建失败！");
    }

    @RequestMapping("/follow")
    @ResponseBody
    public Follow follow(@RequestParam(value = "firstUserId") Long firstUserId, @RequestParam(value = "secondUserId") Long secondUserId) {
        return followRespositiory.addFollow(firstUserId, secondUserId);
    }

    @RequestMapping("/unFollow")
    @ResponseBody
    public void unFollow(@RequestParam(value = "firstUserId") Long firstUserId) {
        followRespositiory.deleteFollows(firstUserId);
        return;
    }

    @RequestMapping("/deleteAll")
    public void deleteAll() {
        followRespositiory.deleteAll();
        return;
    }

    @RequestMapping("/showFollower")
    @ResponseBody
    public List<User> findFollowers(@RequestParam(value = "firstUserId") Long firstUserId) {
        return followRespositiory.findFollowers(firstUserId);
    }
    @RequestMapping(value = "/doubleFollow" , method = RequestMethod.GET)
    @ResponseBody
    public List<User> findDoubleFollowers(@RequestParam(value = "firstUserId") Long firstUserId) {
        return followRespositiory.findFollows(firstUserId);
    }

    @RequestMapping("/showFollow")
    @ResponseBody
    public List<User> findFollows(@RequestParam(value = "firstUserId") Long firstUserId) {
        return followRespositiory.findFollows(firstUserId);
    }

    @RequestMapping(value = "/refresh_follow", method = RequestMethod.GET)
    @ResponseBody
    public List<User> refreshFollow(HttpServletRequest request, HttpSession session) {
        String str = (String) request.getParameter("secondUserId");
        Long secondUserId = Long.parseLong(str);
        if (secondUserId != 0) {
            User user = (User) session.getAttribute("user");
            followRespositiory.deleteFollow(user.getNodeId(), secondUserId);
        }
        session.setAttribute("user",user);
        return followRespositiory.findFollows(user.getNodeId());
    }

    @RequestMapping(value = "/refresh_follower", method = RequestMethod.GET)
    @ResponseBody
    public List<follower> refreshFollower(HttpServletRequest request, HttpSession session) {
        String str = (String) request.getParameter("secondUserId");
        User user = (User) session.getAttribute("user");
        Long firstUserId = user.getNodeId();
        Long secondUserId = Long.parseLong(str);
       List<follower> result = new ArrayList<follower>();
        if (secondUserId != 0) {
            User delete = followRespositiory.findFollow(firstUserId, secondUserId);
            if (delete==null) {
                followRespositiory.addFollow(firstUserId, secondUserId);
            } else {
                followRespositiory.deleteFollow(firstUserId, secondUserId);
            }
        }
        List<User> followers = followRespositiory.findFollowers(user.getNodeId());
         for(int i = 0; i < followers.size();++i){
             User secUser = followers.get(i);
             follower f = new follower();
             f.setUsername(secUser.getUsername());
             f.setNodeId(secUser.getNodeId());
             String relation;
             User tmp =followRespositiory.findFollow(firstUserId,secUser.getNodeId());
            if(tmp!=null)relation="unfollow";
            else relation="follow";
            f.setRelation(relation);
            result.add(f);
         }
         session.setAttribute("user",user);
         return result;
    }

}