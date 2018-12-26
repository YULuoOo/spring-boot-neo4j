// == 值比较  === 类型比较 $(id) ---->  document.getElementById(id)
function $(id){
    return typeof id === 'string' ? document.getElementById(id):id;
}



//定义一个avalonjs的控制器
var follow = avalon.define({
   $id: "follow",
   followlist:{},
    alert:function(secondUserId){
      follow.request(secondUserId);
    },
    request: function (secondUserId) {
        jQuery.ajax({
            type: "get",
            url: "/user/refresh_follow",    //向后端请求数据的url
            data: "secondUserId="+secondUserId,
            dataType:"text",
            success: function (data) {
                var users = JSON.parse(data);
                follow.followlist = users;
            },
            error:function () {
                alert("fail");
            },

        });
    }
});
var follower = avalon.define({
    $id: "follower",
    followerlist:{},
    bt:"follow",
    alert:function(secondUserId){
      follower.request(secondUserId);
    },
    request: function (secondUserId) {
        jQuery.ajax({
            type: "get",
            url: "/user/refresh_follower",    //向后端请求数据的url
            data: "secondUserId="+secondUserId,
            dataType:"text",
            success: function (data) {
                var users = JSON.parse(data);
               follower.followerlist = users;
            },
            error:function () {
                alert("error");
            }
        });
    }
});
var follows = avalon.define({
    $id: "follows",
    followslist:{},
    alert:function(firstUserId){
        follows.request(firstUserId);
    },
    request: function (firstUserId) {
        jQuery.ajax({
            type: "get",
            url: "/user/doubleFollow",    //向后端请求数据的url
            data: "firstUserId="+firstUserId,
            datatype:"text",
            success: function (data) {
                alert(1);
                var users = json.parse(data);
                follows.followslist = users;

            },
            error:function () {
                alert("fail");
            }

        });
    }
});
var tonghao = avalon.define({
    $id: "tonghao",
    tonghaolist: {},
    alert: function (secondUserId) {
        follower.request(secondUserId);
        tonghao.request();
    },
    request:function () {
        jQuery.ajax({
            type: "get",
            url: "/tag/refresh_tonghao",    //向后端请求数据的url
            data:{},
            success: function (data) {
                tonghao.tonghaolist = data;
            },
            error:function () {
                alert("error");
            }
        });
    }
});
var tags = avalon.define({
    $id: "tags",
    taglist:{},
    tag:"",
    submit: function(){
      var str =tags.tag;
      jQuery.ajax({
          type:"get",
          url:"/tag/add_tag",
          dataType:"text",
          data:"tag="+str,
          success:function (data) {
              var result = JSON.parse(data);
              tags.taglist = result;
              tonghao.request();
          },
          error:function () {
              alert("error");
          }
      })
    },
    request: function () {
        jQuery.ajax({
            type: "post",
            url: "/tag/refresh_tags",    //向后端请求数据的url
            data: {},
            success: function (data) {
                tags.taglist = data;
            },
            error:function () {
                alert("error");
            }
        });
    }
});
var search = avalon.define({
    $id: "search",
    userlist:{},
    name:"",
    alert:function(secondUserId){
        follower.request(secondUserId);
        search.submit_name();
    },
    submit_name: function(){
        var str =search.name;
        jQuery.ajax({
            type:"get",
            url:"/search/searchname",
            dataType:"text",
            data:"name="+str,
            success:function (data) {
                var result = JSON.parse(data);
                search.userlist = result;
            },
            error:function () {
                alert("error");
            }

        })
    }


});
// 当页面加载完毕
window.onload = function(){
var i;
    // 拿到所有的标题(li标签) 和 标题对应的内容(div)
    var titles = $('tab-header').getElementsByTagName('li');
var divs = $('tab-content').getElementsByClassName('dom');

// 判断
if(titles.length != divs.length) return;
// 遍历
for( i=0; i<titles.length; i++){
    // 取出li标签
    var li = titles[i];
    li.id = i;
//        console.log(li);
    // 监听鼠标的移动
    li.onmousedown = function(){
        follow.request("0");
        follower.request("0");
        tonghao.request();
        for(var j=0; j<titles.length; j++){
            titles[j].className = '';
            divs[j].style.display = 'none';
        }
        this.className = 'selected';
        divs[this.id].style.display = 'block';
    }
}

follow.request("0");
follower.request("0");
tags.request();
tonghao.request();

  /*  var select = $('dom-search').getElementById('mselect');
    var search_name_div = $('dom-search').getElementById('search_name_div');
    var search_age_div = $('dom-search').getElementById('search_age_div');
    select.onchange= function () {
        alert('changed');
        if(select.value === "name"){
            search_name_div.style.display="inline";
            search_age_div.style.display="none";
        }else if(select.value === 'age'){
            search_age_div.style.display="inline";
            search_age_div.style.display="none";
        }
    }
    */
}

