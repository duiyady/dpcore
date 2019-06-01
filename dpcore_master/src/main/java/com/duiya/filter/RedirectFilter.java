package com.duiya.filter;


import com.alibaba.fastjson.JSONObject;
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
import java.io.IOException;

@WebFilter(filterName = "redirectFilter", urlPatterns = "/*")
public class RedirectFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(RedirectFilter.class);

    @Override
    public void init(FilterConfig filterConfig){
        return;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String uri = request.getRequestURI();
        uri = StringUtil.getRealUri(uri);
        String method = request.getMethod();


        if (uri.contains(".css") || uri.contains(".js") || uri.contains(".otf")
            || uri.contains(".html") ||uri.contains(".eot") || uri.contains(".svg")
            || uri.contains(".ttf") || uri.contains(".woff") || uri.contains(".png")
            || uri.contains(".jpeg") || uri.contains(".jpg")) {
            filterChain.doFilter(request, response);
        }else {

            System.out.println("拦截请求：" + uri);

            if (method.equalsIgnoreCase("get")) {
                if (uri.contains("/file/")) {
                    String op = request.getQueryString();
                    String slaveurl = SlaveMess.getFunctionSlave();

                    System.out.println("get请求重定向：" + slaveurl + uri);

                    if (slaveurl != null) {
                        String u = slaveurl + uri + "?" + op;
                        try {
                            response.sendRedirect(u);
                        } catch (IOException e) {
                            logger.error("转发失败", e);
                        }
                    } else {
                        response.getWriter().write("{\"code\": -5,\"msg\":\"no useful slaves\",\"data\": \"\"}");
                    }
                    return;
                } else if (uri.contains("/test/")) {
                    String slaveurl = SlaveMess.getFunctionSlave();
                    response.getOutputStream().print(slaveurl);
                    return;
                } else {
                    System.out.println("get放行");
                    response.setContentType("textml;charset=UTF-8");
                    response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
                    response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
                    response.setHeader("Access-Control-Max-Age", "0");
                    response.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
                    response.setHeader("Access-Control-Allow-Credentials", "true");
                    response.setHeader("XDomainRequestAllowed", "1");
                    filterChain.doFilter(servletRequest, servletResponse);

                }
            } else if (method.equalsIgnoreCase("post")) {
                if (uri.contains("/file/")) {
                    String slaveurl = SlaveMess.getFunctionSlave();
                    System.out.println("post请求选择转发:" + slaveurl);
                    if (slaveurl != null) {
                        String url = slaveurl + uri;
                        ResponseModel responseModel = null;
                        try {
                            responseModel = HttpUtil.transmitPost(url, request);
                        } catch (IOException e) {
                            System.out.println("那个服务器不可用");
                        }
                        if (responseModel.getCode() != ResponseEnum.UNKNOEN_ERROR) {
                            try {
                                response.getOutputStream().print(JSONObject.toJSONString(responseModel));
                                response.getOutputStream().flush();
                                response.getOutputStream().close();
                            } catch (IOException e) {
                                logger.error("转发失败");
                            }
                        }
                    } else {
                        response.getWriter().write("{\"code\": -5,\"msg\":\"系统发生错误\",\"data\": \"\"}");
                    }
                } else {
                    System.out.println("post 放行");
                    response.setContentType("textml;charset=UTF-8");
                    response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
                    response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
                    response.setHeader("Access-Control-Max-Age", "0");
                    response.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
                    response.setHeader("Access-Control-Allow-Credentials", "true");
                    response.setHeader("XDomainRequestAllowed", "1");
                    filterChain.doFilter(servletRequest, servletResponse);
                }
            } else {
                filterChain.doFilter(servletRequest, servletResponse);
            }
        }
    }

    @Override
    public void destroy() {
        System.out.println("destory");
    }
}
