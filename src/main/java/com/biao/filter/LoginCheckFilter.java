package com.biao.filter;

import com.alibaba.fastjson.JSON;
import com.biao.common.BaseContext;
import com.biao.common.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //路径匹配器
    private final static AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        log.info("拦截的路径{}",request.getRequestURI());
        String uri = request.getRequestURI();
        //无需处理的路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
        };


        if (check(urls, uri)) {
            filterChain.doFilter(request, response);
            log.info("静态资源");
            return;
        }
        HttpSession session = request.getSession();
        Long employee = (Long)session.getAttribute("employee");
        if (employee == null) {
            response.getWriter().write(JSON.toJSONString(ResponseResult.error("NOTLOGIN")));
            return;
        }
//        log.info("线程id:{}",Thread.currentThread().getId());
        //一个请求一个线程，线程具有隔离性
        BaseContext.setCurrentId(employee);
        filterChain.doFilter(request, response);


    }

    public boolean check(String[] urls, String uri) {
        for (int i = 0; i < urls.length; i++) {
            if (PATH_MATCHER.match(urls[i], uri) == true)
                return true;

        }
        return false;
    }
}
