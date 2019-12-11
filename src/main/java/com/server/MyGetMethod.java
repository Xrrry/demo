package com.server;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@Api(value="/",description = "全部的get方法")
public class MyGetMethod {

    @RequestMapping(value = "/getCookies",method = RequestMethod.GET)
    @ApiOperation(value="获取到cookies值",httpMethod="GET")
    public String getCookies(HttpServletResponse response){
        Cookie cookie = new Cookie("login","true");
        response.addCookie(cookie);
        return "getcookies";
    }
    @RequestMapping(value = "/get",method = RequestMethod.GET)
    @ApiOperation(value="简单get",httpMethod="GET")
    public String get(){
        return "success";
    }

    @RequestMapping(value = "/getwithcookies",method = RequestMethod.GET)
    @ApiOperation(value="携带cookies的get",httpMethod="GET")
    public String getwithcookies(HttpServletRequest request){
        Cookie[] cookie = request.getCookies();
        if(Objects.isNull(cookie)){
            return "必须携带cookies";
        }
        for (Cookie cookie1:cookie){
            if(cookie1.getName().equals("login") && cookie1.getValue().equals("true")){
                return "访问成功";
            }
        }
        return "必须携带正确的cookies";
    }

    @RequestMapping(value = "/getwithparam",method = RequestMethod.GET)
    @ApiOperation(value="携带param的get",httpMethod="GET")
    public Map<String,Integer> getList(@RequestParam Integer start,
                                       @RequestParam Integer end){
        Map<String,Integer> myList = new HashMap<>();

        myList.put("鞋",400);
        myList.put("干脆面",1);
        myList.put("衬衫",300);
        return  myList;
    }

    @RequestMapping(value = "/getwithparam/{start}/{end}",method = RequestMethod.GET)
    @ApiOperation(value="方法2",httpMethod="GET")
    public Map<String,Integer> mygetList(@PathVariable Integer start,
                                         @PathVariable Integer end){
        Map<String,Integer> myList = new HashMap<>();

        myList.put("鞋",400);
        myList.put("干脆面",1);
        myList.put("衬衫",300);
        return  myList;
    }
}
