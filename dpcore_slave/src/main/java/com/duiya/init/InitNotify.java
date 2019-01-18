package com.duiya.init;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;

/**
 * 进行通知
 */
@WebListener
public class InitNotify implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
       // RedisCache redisCache = new RedisCache();
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {

        /*创建目录*/
        File file = new File(DPCoreInit.ROOT_LOCATION);
        if(!file.exists() && !file.isDirectory()){
            file.mkdirs();
        }

    }
}
