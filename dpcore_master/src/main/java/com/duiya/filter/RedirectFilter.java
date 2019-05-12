package com.duiya.filter;


import com.duiya.init.SlaveMess;
import com.duiya.model.ResponseModel;
import com.duiya.utils.HttpUtil;
import com.duiya.utils.ResponseEnum;
import com.duiya.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;

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
        uri = StringUtil.getRealUri(uri);
        String method = request.getMethod();
        System.out.println("收到请求：" + request.getRequestURL());
        if (method.equalsIgnoreCase("get")) {
            if (uri.contains("/file") || uri.contains("/user")) {
                System.out.println("拦截到get请求重定向：" + request.getRequestURL());
                String op = request.getQueryString();
                String slaveurl = SlaveMess.getFunctionSlave();
                String u;
                if(slaveurl != null) {
                    u = slaveurl + uri + "?" + op;
                    try {
                        response.sendRedirect(u);
                    } catch (IOException e) {
                        logger.error("转发失败", e);
                    }
                }else{
                    u = "";
                }
                return;
            }else if(uri.contains("/hhh")){
                Enumeration<String> hea = request.getHeaderNames();
                while(hea.hasMoreElements()){
                    String name = hea.nextElement();
                    String val = request.getHeader(name);
                    System.out.println(name + ": " + val);
                }
                BufferedReader bf = new BufferedReader(new InputStreamReader(request.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String lin;
                while((lin = bf.readLine()) != null){
                    sb.append(lin);
                }
                System.out.println(sb.toString());
                System.out.println(request.getQueryString());
                response.getOutputStream().print("success");
            }else{
                System.out.println("get放行");
                filterChain.doFilter(servletRequest, servletResponse);
            }
        } else if (method.equalsIgnoreCase("post")) {
            if (uri.contains("/file") || uri.contains("/user")) {
                String slaveurl = SlaveMess.getFunctionSlave();
                System.out.println("选择的url:" + slaveurl);
                if(slaveurl != null) {
                    String url = slaveurl + uri;
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
                }else{
                    response.getWriter().write("{\"code\": -5,\"msg\":\"系统发生错误\",\"data\": \"\"}");
                }
            }else if(uri.contains("/hhh")){
                Enumeration<String> hea = request.getHeaderNames();
                while(hea.hasMoreElements()){
                    String name = hea.nextElement();
                    String val = request.getHeader(name);
                    System.out.println(name + ": " + val);
                }
                BufferedReader bf = new BufferedReader(new InputStreamReader(request.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String lin;
                while((lin = bf.readLine()) != null){
                    sb.append(lin);
                }
                System.out.println(sb.toString());
            }else {
                System.out.println("post 放行");
                filterChain.doFilter(servletRequest, servletResponse);
            }
        }else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {
        System.out.println("destory");
    }
}
