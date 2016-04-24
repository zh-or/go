package com.go.base;

import java.io.File;

import util.cluster.client.NodeSession;
import util.config.Config;
import util.format.Format;
import util.log.Log;
import util.socket.server.Server;
import util.util.Util;

import com.go.common.C;
import com.go.common.MsgHandler;

public class Context {

    private static Object lock = new Object();
    private static Context ctx;
    private Log log;
    private Server server;
    private Config ini;
    private NodeSession mqnode;
    
    public Log getLog() {
        return log;
    }

    public Server getServer() {
        return server;
    }

    public Config getIni() {
        return ini;
    }

    private Context() {
        log = Log.getInstance();
        log.setProjectName("service-main");
    }

    public static Context getInstance() {
        if (ctx == null) {
            synchronized (lock) {
                if (ctx == null) {
                    ctx = new Context();
                }
            }
        }
        return ctx;
    }

    public void init(String confpath) {
        log.info("read config...");
        ini = new Config(new File(confpath));
        ini.read();
        log.info("config info :" + ini.toString());
        try {
            int port = ini.getIntValue(C.INI_MAIN, C.INI_PORT, 5002);
            for(;;){
                try {
                    server = new Server(
                                ini.getStringValue(C.INI_MAIN, C.INI_HOST, "0.0.0.0"),
                                port,
                                ini.getIntValue(C.INI_MAIN, C.INI_TCOUNT, 100),
                                ini.getIntValue(C.INI_MAIN, C.INI_BUFF, 1024),
                                ini.getIntValue(C.INI_MAIN, C.INI_IDLE, 3) * 60,
                                new MsgHandler()
                            );
                    log.info("starting connection service, bind port: %d", server.getPort());
                    server.start();
                    break;
                } catch (Exception e) {}
                log.warn("bind port %d fail, port change to %d", port, ++port);
                
            }
            mqnode = new NodeSession(
                                    ini.getStringValue(C.INI_GWY, C.INI_HOST, "gateway.service.qlx8.com"), 
                                    ini.getIntValue(C.INI_GWY, C.INI_PORT, 5000)
                                  );
            int i = 3;//test conn total;
            for(;;){
                log.info(
                        "message queue service host: %s, port: %d", 
                        ini.getStringValue(C.INI_GWY, C.INI_HOST, "gateway.service.qlx8.com"),
                        ini.getIntValue(C.INI_GWY, C.INI_PORT, 5000)
                        );
                boolean isinit = mqnode.init(ini.getIntValue(C.INI_GWY, C.INI_CONN_TIMEOUT, 1000 * 20));
                if(isinit) break;
                log.error("failed connection geteway service. 3 seconds try again...");
                i--;
                if(i <= 0){
                    throw new Exception("connection geteway error");
                }
                Util.SLEEP(3000);
            }
            log.info("mq node id: %s", mqnode.getNodeId());
            log.info("server start success...");
        } catch (Exception e) {
           log.error("start failure:" + Format.formatException(e));
           System.exit(1);
        }
    }

}
