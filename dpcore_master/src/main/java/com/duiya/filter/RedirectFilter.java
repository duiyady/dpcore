package com.duiya.filter;


import com.duiya.model.ResponseModel;
import com.duiya.utils.HttpUtil;
import com.duiya.utils.ResponseEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "redirectFilter", urlPatterns = "/*")
public class RedirectFilter implements Filter {
    private Logger logger = LoggerFactory.getLogger(RedirectFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        return;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String uri = request.getRequestURI();
        String method = request.getMethod();
        System.out.println("收到请求：" + request.getRequestURL());
        if (method.equalsIgnoreCase("get")) {
            if (uri.contains("/fileop/*") || uri.contains("/user/*")) {
                System.out.println("拦截到get请求重定向：" + request.getRequestURL());
                String op = request.getQueryString();
                String u = "http://localhost:10086/dpcore-slave" + uri + "?" + op;
                try {
                    response.sendRedirect(u);
                } catch (IOException e) {
                    logger.error("转发失败", e);
                }
                return;
            }
        } else if (method.equalsIgnoreCase("post")) {
            if (uri.contains("/fileop/*") || uri.contains("/user/*")) {
                String url = "http://localhost:10086/dpcore-slave" + uri;
                ResponseModel responseModel = null;
                try {
                    responseModel = HttpUtil.transmitPost(url, request);
                } catch (IOException e) {
                    System.out.println("那个服务器不可用");
                }
                if (responseModel.getCode() != ResponseEnum.UNKNOEN_ERROR) {
                    try {
                        response.getOutputStream().print(responseModel.toString());
                        response.getOutputStream().flush();
                        response.getOutputStream().close();
                    } catch (IOException e) {
                        logger.error("转发失败");
                    }
                }
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        System.out.println("destory");
    }
}