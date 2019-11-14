package com.cpren.controller;

import com.cpren.pojo.User;
import com.cpren.utils.YWJsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * @author cdxu@iyunwen.com on 2019/10/23
 */
@Controller
@Slf4j
public class LoginController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * @describe 跳转到登录页面
     * @author cp.ren
     * @date 2019-10-23 16:26:10
     * @return
     * @version V5.0
     **/
    @RequestMapping("/toLogin")
    public ModelAndView ToLogin(@RequestParam String oldUrl){
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("oldUrl", oldUrl);
        return modelAndView;
    }

    /**
     * @describe 登录校验
     * @author cp.ren
     * @date 2019-10-23 16:26:28
     * @return
     * @version V5.0
     **/
    @PostMapping(value = "/login", consumes = "application/x-www-form-urlencoded")
    public void login(@RequestParam String userName,@RequestParam String password, @RequestParam String oldUrl, HttpServletResponse response) {
        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        user.setOldUrl(oldUrl);

        // 生成token，这里简单一点了直接用uuid
        String uuid = UUID.randomUUID().toString().replaceAll("-","");

        if("rcp".equals(user.getUserName()) && "123".equals(user.getPassword())) {
            log.info("用户登录验证成功！");
            user.setAuths(new ArrayList<>());
            user.getAuths().add("/cpren/user");
            user.getAuths().add("/cpren2/user");
            try {
                stringRedisTemplate.opsForValue().set(uuid, YWJsonUtils.getMapper().writeValueAsString(user));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            Cookie cookie = new Cookie("token", uuid);
            cookie.setMaxAge(2*60*60*1000);
            response.addCookie(cookie);
            try {
                response.sendRedirect(user.getOldUrl()+"?token="+uuid);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            try {
                response.sendRedirect("http://www.sso.com:9999/sso/toLogin?oldUrl=" + oldUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @describe 校验和客户端之前是否存在token
     * @author cp.ren
     * @date 2019-10-23 16:26:48
     * @param request
     * @param oldUrl
     * @return
     * @version V5.0
     **/
    @RequestMapping("/findToken")
    public void findToken(HttpServletRequest request,HttpServletResponse response, String oldUrl) {
        String token = "";
        Cookie[] cookies = request.getCookies();
        if(!StringUtils.isEmpty(cookies)){
            for (Cookie cookie : cookies) {
                if("token".equals(cookie.getName())) {
                    log.info("在认证中心找到token信息！");
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if(!"".equals(token)){
            try {
                response.sendRedirect(oldUrl+"?token="+token);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            log.info("认证中心不存在token信息，即将跳转到登录页面");
            try {
                response.sendRedirect("http://www.sso.com:9999/sso/toLogin?oldUrl=" + oldUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
