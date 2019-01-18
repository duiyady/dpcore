package com.duiya.filter;


import com.duiya.model.ResponseModel;
import com.duiya.utils.ResponseEnum;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "redirectFilter", urlPatterns = "/*")
public class RedirectFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        return;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String uri = request.getRequestURI();
        String op = request.getQueryString();
        if (uri.contains("/fileop/get")) {
            String u = "http://www.duiyy.cn/dpcore-slave" + uri + "?" + op;
            System.out.println(u);
            response.sendRedirect(u);
            return;
        } else if (uri.contains("/fileop/testGet")) {
            String u = "http://www.duiyy.cn/dpcore-slave" + uri;
            System.out.println(u);
            response.sendRedirect(u);
            return;
        } else if (uri.contains("/fileop/testPost")) {
            String u = "http://www.duiyy.cn/dpcore-slave" + uri;
            System.out.println(u);
            request.getRequestDispatcher(u).forward(servletRequest, servletResponse);
        } else if (uri.contains("/test")) {
            System.out.println(request.getRequestURI() + "==================" + Thread.currentThread().getName());
            if (request.getMethod().equalsIgnoreCase("post")) {

            } else {

                filterChain.doFilter(servletRequest, servletResponse);
            }
        }else if(uri.contains("/fileop/upload")){
            String url = "http://www.duiyy.cn/dpcore-slave" + uri;
            ResponseModel responseModel = MyHttpUtils.transmitPost(url, request);
            if(responseModel.getCode() != ResponseEnum.UNKNOEN_ERROR){
                response.getOutputStream().print(responseModel.toString());
                response.getOutputStream().flush();
                response.getOutputStream().close();
            }

        }else{
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {
        System.out.println("destory");
    }
}
