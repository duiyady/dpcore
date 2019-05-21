package com.duiya.init;

import com.duiya.utils.PropertiesUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;

@WebListener
public class Initialize implements ServletContextListener {

    public static PropertiesUtil propertiesUtil = new PropertiesUtil("dpcore-single.properties");


    @Override
    public void contextDestroyed(ServletContextEvent arg0) {

    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        String root_location = propertiesUtil.getStringValue("dpcore.location");
        if(root_location == null){
            root_location = "/dpcore/file/";
        }
        BaseConfig.ROOT_LOCATION = root_location;
        /*创建目录*/
        File file = new File(BaseConfig.ROOT_LOCATION);
        if(!file.exists() && !file.isDirectory()){
            file.mkdirs();
        }

        Boolean verifide = propertiesUtil.getBooleanValue("dpcore.verified");
        if(verifide == null){
            verifide = false;
        }
        BaseConfig.VERIFIED = verifide;
    }

}
