package com.qlx8.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import javax.servlet.ServletContextEvent;

import lotus.cluster.Message;
import lotus.cluster.node.MessageHandler;
import lotus.cluster.node.NodeSession;
import lotus.config.Config;
import lotus.format.Format;
import lotus.log.Log;
import lotus.mq.IMQBase;
import lotus.mq.MessageQueue;
import lotus.mq.MessageQueue.MessageTimeOut;
import lotus.util.Util;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.qlx8.model.LoginLog;
import com.qlx8.model.SmsCode;
import com.qlx8.util.C;

public class MainService {
    private static       MainService    service;
    private static       Object         lock_this  = new Object();
    
    private Config                 conf;
    private Log 	               log;
    private String                 rootPath        = "";
	private DruidDataSource        dataSource;
	private File                   fdir_cache      = null;
	private File                   fdir_raiders    = null;
	private MessageQueue           mqueue          = null;
	private Object                 lock_loginlog   = null;
	private ArrayList<LoginLog>    llogs           = null;
	private NodeSession            managersession  = null;
	
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
        log.setProjectName("旅行-DB");
        lock_loginlog = new Object();
        llogs = new ArrayList<LoginLog>();
        mqueue = new MessageQueue(new MessageTimeOut() {
            @Override
            public void timeisup(IMQBase msg) {
                SmsCode smscode = (SmsCode) msg;
                log.warn("短信验证码超时, token: %s, smscode: %s", smscode.phone, smscode.codevalue);
            }
        }, 2000);
        
    }
    
    public void init(ServletContextEvent contextEvent){
        rootPath = contextEvent.getServletContext().getRealPath("/") + "/WEB-INF/";
        fdir_cache = new File(rootPath + C.DIR.SysBase + "/" + C.DIR.STATE_CACHE + "/");
        fdir_raiders = new File(rootPath + C.DIR.SysBase + "/" + C.DIR.RAIDERS + "/");
        if(!fdir_cache.exists()) fdir_cache.mkdirs();
        if(!fdir_raiders.exists()) fdir_raiders.mkdirs();
        
        log.info("读取配置文件...");
        conf = new Config(new File(rootPath + "config.ini"));
        conf.read();
        log.debug(conf.toString());
        
        log.info("初始化数据库连接池...");
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(new File(rootPath + "dbconf.properties")));
            dataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
            dataSource.getConnection().close();
        } catch (Exception e) {
            log.info("初始化数据库连接池失败...");
            log.error(Format.formatException(e));
        }
        
        log.info("初始化消息系统...");

        managersession = new NodeSession(
                conf.getStringValue(C.INI.MANAGER, C.INI.IP, "127.0.0.1"),
                conf.getIntValue(C.INI.MANAGER, C.INI.PORT, 5000));
        
    }
    
	
	public boolean initManager(){
	    
	    boolean isinit = managersession.init(
	            conf.getIntValue(C.INI.MANAGER, C.INI.CONN_TIMEOUT, 20 * 1000));
	    if(isinit){
	        managersession.setHandler(eventhandler);
	    }
	    return isinit;
	}
	
    public void uninit() {
        try{
            if(dataSource != null)
                dataSource.close();
            if(managersession != null)
                managersession.close();
            if(mqueue != null)
                mqueue.close();
        }catch(Exception e){
            
        }
    }
	
	private MessageHandler eventhandler = new MessageHandler() {

    };
	
    public NodeSession getMessageQueueSession(){
        return managersession;
    }
	
	public Connection getDbConn() throws SQLException{
		return dataSource.getConnection();
	}
	
    public Config getConfig(){
        return conf;
    }

    public Log getLog(){
        return log;
    }
    
    /**
     * 默认超时时间为 3 min
     * @param token
     * @param codevalue
     */
    public void pushSmsCode(String token, String codevalue){
        SmsCode smscode = new SmsCode(token, codevalue);
        smscode.setTimeOut(conf.getIntValue(C.INI.MAIN, C.INI.SMSCODE_TIMEOUT, 3 * 60 * 1000));/*短信超时时间为3min*/
        mqueue.addMessage(smscode);
    }
    
    public SmsCode getSmsCode(String token){
        return (SmsCode) mqueue.getMessage(token);
    }
    
    public void removeSmsCode(String token){
        mqueue.remove(token);
    }
    
    /**
     * 返回WebContent目录下的WEB-INF的路径
     * @return 
     */
    public String getRootPath(){
        return rootPath;
    }

    
    public File getCacheDir(){
        return fdir_cache;
    }
    
    public File getRaidersDir(){
        return fdir_raiders;
    }

    public void addLoginLog(LoginLog llog){
        synchronized (lock_loginlog) {
            llogs.add(llog);
        }
    }
    
    public void commentRunningLog(String type, String str){
        if(managersession != null){
            Message msg = new Message(  Message.TYPE_SUBSCRIBE_MESSAGE,
                                type,
                                "",
                                Util.getUUID(),
                                type,
                                str.getBytes());
            try {
                managersession.pushMessage(msg);
            } catch (Exception e) {}
        }
    }
    
    /**
     * 创建一个旅行的编号
     * @return
     */
    public int createLxNumber(){
        /*从1k开始, 每次随机+个数*/
        return (int) System.currentTimeMillis();
    }



    
    
    
}
