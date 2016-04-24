package com.go.service;

import java.io.File;

import javax.servlet.ServletContextEvent;

import util.cluster.server.ClusterService;
import util.config.Config;
import util.log.Log;

public class MainService {
    private static MainService service;
    private static Object    lock_this = new Object();
    private Config conf;
    private Log log;
    private String rootPath = "";
    private ClusterService mqService;
    
    public static MainService getInstanse(){
        if(service == null){
            synchronized (lock_this) {
                if(service == null){
                    service = new MainService();
                }
            }
        }
        return service;
    }
    
    private MainService(){
        log = Log.getInstance();
    }

    
    public void initConfig(String path) throws Exception{
        initConfig(new File(path));
    }
    
    public void initConfig(File in) throws Exception{
        conf = new Config(in);
        conf.read();
    }
    
    public void initRootPath(ServletContextEvent contextEvent){
        rootPath = contextEvent.getServletContext().getRealPath("/");
    }
    
    public void initClusterService(String host, int port) throws Exception{
        mqService = new ClusterService(host, port);
        mqService.start();
    }
    
    public Config getConfig(){
        return conf;
    }

    public Log getLog(){
        return log;
    }
    
    public String getRootPath(){
        return rootPath;
    }

}
