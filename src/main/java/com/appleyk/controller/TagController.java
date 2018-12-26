package com.appleyk.controller;
import com.appleyk.model.Like;
import com.appleyk.model.Tag;

import com.appleyk.model.User;
import com.appleyk.model.follower;
import com.appleyk.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/tag")
public class TagController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    FollowRespository followRespository;
    @Autowired
    tagRepository tagRepository;
    @Autowired
    LikeRespository likeRespository;

    private Tag tag = new Tag();
    private Like like = new Like();

    @RequestMapping(value = "refresh_tags", method = RequestMethod.POST)
    @ResponseBody
    public List<Tag> refreshTags(HttpSession session) {
        User user = (User) session.getAttribute("user");
        List<Tag> result = likeRespository.findLikes(user.getNodeId());
        session.setAttribute("user",user);
        return result;
    }

    @RequestMapping(value = "add_tag", method = RequestMethod.GET)
    @ResponseBody
    public List<Tag> addTags(HttpSession session, HttpServletRequest request) {
        User user = (User) session.getAttribute("user");
        //上面这三句是因为这时候session里面的user的tags是没有更新的状态，需要手动提取id再从数据库取user
        String new_tag = request.getParameter("tag");
        tag = tagRepository.findTagByName(new_tag);
        if(tag == null){
            tagRepository.addTag(new_tag);
        }
        likeRespository.addLike(user.getNodeId(), new_tag);
        List<Tag> result = likeRespository.findLikes(user.getNodeId());
        session.setAttribute("user",user);
        return result;
    }

    @RequestMapping(value = "/refresh_tonghao", method = RequestMethod.GET)
    @ResponseBody
    public List<follower> refreshTonghao(HttpServletRequest request, HttpSession session) {
        User user = (User)session.getAttribute("user");
        List<Tag> likes = likeRespository.findLikes(user.getNodeId());
        List<follower> followerlist = new ArrayList<follower>();
        List<User> result = new ArrayList<User>();
        for(int i = 0 ; i < likes.size();++i){
            List<User> list = likeRespository.findLikers(likes.get(i).getName());
            for(int j = 0 ;j < list.size();++j){
                if(list.get(j).getNodeId()==user.getNodeId())
                    list.remove(j);
                for(int k = 0 ; k < result.size();++k){
                    if(list.get(j).getNodeId()==result.get(k).getNodeId())
                        list.remove(j);
                }
            }
            result.addAll(list);
        }
        for(int i = 0; i < result.size();++i){
            User secUser = result.get(i);
            follower f = new follower();
            f.setUsername(secUser.getUsername());
            f.setNodeId(secUser.getNodeId());
            String relation;
            User tmp =followRespository.findFollow(user.getNodeId(),secUser.getNodeId());
            if(tmp!=null)relation="unfollow";
            else relation="follow";
            f.setRelation(relation);
            followerlist.add(f);
        }
        session.setAttribute("user",user);
        return followerlist;


    }

}
