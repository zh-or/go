package com.qlx8.util;

public class C {
    
    public final class DEVICES_TYPE{
        public final static String WEB              =  "web";
        public final static String MOBILE           =   "mobile";
        public final static String MOBILE_ANDROID   =   "android";
        public final static String MOBILE_IPHONE    =   "iphone";
    }
    
    public final class SYS{
        public final static String SALT             =   "= hello _ world =";

        public final static int     SEX_MAN         =   1;
        public final static int     SEX_FEMALE      =   0;
        public final static int     SEX_OTHER       =   -1;
        public final static String  HEAD_DEF        =   "def_head_img.png";/*默认头像*/
    }
    
    public final class SUBSCRIBETYPE{
        public final static String  ExceptionLog    =   "Exception log";
        public final static String  REG_SMS_CODE    =   "sms code";
    }
    
    public final class DIR{
        /**
         * 系统根目录
         */
        public final static String SysBase          =   "System";
        
        /**
         * 下面子目录按id分类 每个子目录存1k用户的东西, 每个用户1个目录 目录名字为用户id
         */
        public final static String RAIDERS          =   "Raiders";/*路书/攻略保存目录 md文档?*/
        
        /**
         * 缓存目录
         */
        public final static String STATE_CACHE      =   "Cache";
        
        public final static String LvxingAttachment =   SysBase + "/Images/LvxingAttachment/";
        public final static String HeadImages       =   SysBase + "/Images/HeadImages/";
        
    }
    
    public final class INI{
        public final static String MAIN             =   "MAIN";
        public final static String ENCODEING        =   "ENCODEING";
        public final static String CLUSTER_HOST     =   "CLUSTER_HOST";
        public final static String CLUSTER_PORT     =   "CLUSTER_PORT";
        public final static String SMSCODE_TIMEOUT  =   "SMS_CODE_TIMEOUT";
        
        public final static String MANAGER          =   "MANAGER";
        public final static String IP               =   "IP";
        public final static String PORT             =   "PORT";
        public final static String CONN_TIMEOUT     =   "CONN_TIMEOUT";
    }
}
