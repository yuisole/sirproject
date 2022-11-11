package com.yue.config.config.JWT;

import com.alibaba.fastjson.JSON;

import com.yue.config.Msg;
import com.yue.config.config.JSON.JsonResult;
import com.yue.config.config.JSON.JwtUtils;
import com.yue.config.config.JSON.ResultTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
public class JWTAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtUtils jwtUtils;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
    }

//    闲置，改用登陆接口返回token
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        //生成JWT,并放置到请求头中
        String jwt = jwtUtils.generateToken(authentication.getName());
        response.setHeader("token",jwt);

//        获取权限
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        判断身份
//        SimpleGrantedAuthority是GrantedAuthority的实现类

        boolean role = authorities.contains(new SimpleGrantedAuthority("ROLE_admin"));

        Msg msg;
        if (role){
            msg = Msg.success("登陆成功！").add("token",jwt).add("role",9999);
        }
        else {
            msg = Msg.success("登陆成功！").add("token",jwt).add("role",1111);
        }
        //处理编码方式，防止中文乱码的情况
        response.setContentType("text/json;charset=utf-8");
        //塞到HttpServletResponse中返回给前台
        response.getWriter().write(JSON.toJSONString(msg));
    }
}
