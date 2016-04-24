package com.go.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import util.format.Format;
import util.log.Log;

import com.go.service.MainService;
import com.go.util.C;

public class StartupListener implements ServletContextListener {
    private MainService service = null;
    
    @Override
    public void contextInitialized(ServletContextEvent contextEvent) {
        service = MainService.getInstanse();
        try {
            
            service.getLog().log("初始化...");
            service.initRootPath(contextEvent);
            service.initConfig(service.getRootPath() + "/WEB-INF/config.ini");
            service.initClusterService(
                    service.getConfig().getStringValue(C.INI.MAIN, C.INI.CLUSTER_HOST, "0.0.0.0"),
                    service.getConfig().getIntValue(C.INI.MAIN, C.INI.CLUSTER_PORT, 6000));
        } catch (Exception e) {
            service.getLog().log(Log.L_E, "初始化失败:%s", Format.formatException(e));
        }
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent contextEvent) {
        service.getLog().log(Log.L_W, "服务正在结束...");
        service.getConfig().save();
        service.uninitClusterService();
    }
}
