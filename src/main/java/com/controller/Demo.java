package com.controller;

import com.bean.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@Api(value = "/",description = "这是我的第一个版本的demo")
public class Demo {

    //首先获取一个执行sql语句的对象

    @Autowired
    private SqlSessionTemplate template;

    @RequestMapping(value = "/getUserCount",method = RequestMethod.GET)
    @ApiOperation(value = "可以获取到用户数",httpMethod = "GET")
    public int getUserCount(){
        return template.selectOne("getUserCount");
    }

    @RequestMapping(value = "/addUser",method = RequestMethod.POST)
    public int addUser(@RequestBody User user) {
        int result = template.insert("addUser", user);
        return result;
    }

    @RequestMapping(value = "/updateUser",method = RequestMethod.POST)
    public int updateUser(@RequestBody User user){
        return  template.update("updateUser",user);
    }

    @RequestMapping(value = "/deleteUser",method = RequestMethod.GET)
    public int delUser(@RequestParam int id){
        return template.delete("deleteUser",id);
    }

    @RequestMapping(value = "/query",method = RequestMethod.POST) // 用户查询
    public String query(@RequestParam String out_id){
        SecondController s = new SecondController();
        return s.query(out_id);
    }

    @RequestMapping(value = "/perHistory",method = RequestMethod.POST) // 查询记录
    public String perHistory(@RequestParam String cus_acc){
        SecondController s = new SecondController();
        return s.perAllHistory(cus_acc);
    }

    @RequestMapping(value = "/proAllCom",method = RequestMethod.POST) // 厂商所有模板
    public String proAllCom(@RequestParam String pro_acc){
        SecondController s = new SecondController();
        return s.proAllCom(pro_acc);
    }

    @RequestMapping(value = "/upOut",method = RequestMethod.POST) // 厂商上传出库
    public String upOut (@RequestParam String out_id,String pro_acc, String com_id, String out_time){
        SecondController s = new SecondController();
        return s.upOut(out_id,pro_acc,com_id,out_time);
    }

    @RequestMapping(value = "/loginSearch",method = RequestMethod.POST) // 登录验证
    public String loginSearch(@RequestParam String phone){
        SecondController s = new SecondController();
        return s.loginSearch(phone);
    }
    @RequestMapping(value = "/upCom",method = RequestMethod.POST) // 上传模板
    public String upCom(@RequestParam String pro_acc, String name, String type, String price, String locate){
        SecondController s = new SecondController();
        return s.upCom(pro_acc,name,type,price,locate);
    }
}