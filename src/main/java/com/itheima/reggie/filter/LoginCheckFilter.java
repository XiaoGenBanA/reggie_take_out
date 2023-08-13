package com.itheima.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {

    public static final AntPathMatcher PATH_MATCHER= new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse)servletResponse;
//      log.info("拦截到请求：{}",httpServletRequest.getRequestURI());
        //1、获取本次请求的URI；2、定义不需要的请求路径
        String requestURI = httpServletRequest.getRequestURI();

        String[] urls = new String[]{
          "/employee/login",
          "/employee/logout",
          "/backend/**",
          "/front/**",
        };

        boolean check = check(urls, requestURI);
        if (check) {
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }

        if (httpServletRequest.getSession().getAttribute("employee") != null){
            Long empId = (Long) httpServletRequest.getSession().getAttribute("employee");
            BaseContext.setId(empId);
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }

        httpServletResponse.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));

    }

    public  boolean check(String[] urls,String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match){
                return true;
            }
        }
        return false;
    }
}
