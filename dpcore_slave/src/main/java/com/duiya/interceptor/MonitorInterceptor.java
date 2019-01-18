package com.duiya.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 这个拦截器在测试时用来监控时间
 */
public class MonitorInterceptor extends HandlerInterceptorAdapter {
    private Logger logger = LoggerFactory.getLogger(MonitorInterceptor.class);

    @Override
    //preHandle方法，在请求发生前执行。
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getRequestURI().contains("/fileop/upload")
                || request.getRequestURI().contains("/fileop/get")) {
            long startTime = System.currentTimeMillis();
            request.setAttribute("startTime", startTime);
        }
        return true;
    }

    @Override
    //postHandle方法，在请求完成后执行。
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (request.getRequestURI().contains("/fileop/upload")
                || request.getRequestURI().contains("/fileop/get")) {
            long startTime = (Long) request.getAttribute("startTime");
            request.removeAttribute("startTime");
            long endTime = System.currentTimeMillis();
            //这里的选择写入日志还是动态分析
            logger.info("requesr:" + request.getRequestURI()+ "--->" + new Long(endTime - startTime) + "ms");
            System.out.println(request.getRequestURI() + " -->" + new Long(endTime - startTime)+"ms");
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        super.afterConcurrentHandlingStarted(request, response, handler);
    }
}
