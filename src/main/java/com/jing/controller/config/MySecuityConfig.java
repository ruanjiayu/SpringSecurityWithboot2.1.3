package com.jing.controller.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author ruanjiayu
 * @dateTime 2019/4/3 19:32
 */

@EnableWebSecurity
public class MySecuityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //定制请求的授权规则
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/level1/**").hasRole("VIP1")
                .antMatchers("/level2/**").hasRole("VIP2")
                .antMatchers("/level3/**").hasRole("VIP3");
        //开启自动配置的登录功能
        http.formLogin()
                .loginPage("/userlogin")
                .usernameParameter("username")
                .passwordParameter("pwd");
        //1. /login来到登录页面，可以自己定义使用.loginPage来设置，
        //2. 重定向到/login?error表示登录失败
        //usernameParameter("username")和passwordParameter("pwd")表示HTML页面中提交数据的namez值



        //开启自动配置注销功能
        //注销成功以后来到自己定义的首页
        http.logout().logoutSuccessUrl("/");
        //1.访问/logout 表示用户注销，清空session
        //一旦注销，哪怕你使用原来的cookie，经过尝试也不能进行登录了，说明服务端的session被清理了



        http.rememberMe().rememberMeParameter("rememberMe");
        //后面的rememberMeParameter("rememberMe")表示的是在HTML页面中的name值

    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        //注意spring security 5.0版本后面新增了多种加密方式，改变了密码的格式，需要你使用passwordEncoder来进行加密
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .withUser("zhansan").password(new BCryptPasswordEncoder().encode("123456")).roles("VIP1","VIP2")
                .and()
                .withUser("lisi").password(new BCryptPasswordEncoder().encode("123456")).roles("VIP2","VIP3")
                .and()
                .withUser("wangwu").password(new BCryptPasswordEncoder().encode("123456")).roles("VIP1","VIP3");
    }
}
