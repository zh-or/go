package com.qlx8.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import lotus.log.Log;

import com.qlx8.service.MainService;

public class StartupListener implements ServletContextListener {
    private MainService service = null;
    private Log         log     = null;
    @Override
    public void contextInitialized(ServletContextEvent contextEvent) {
        service = MainService.getInstanse();
        log = service.getLog();
        log.info("初始化服务器...");
        service.init(contextEvent);
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent contextEvent) {
        log.warn("服务正在结束...");
        service.getConfig().save();
        service.uninit();
    }
}
