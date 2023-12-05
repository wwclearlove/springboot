package com.glasssix.dubbo.constant;

public class Constants {
    /**
     * 自增id
     */
    public static final String GLASS_GLOBAL_ID = "GLASS:GLOBAL:ID";
    /**
     * 账号锁定
     */
    public static final String ACCOUNT_LOCK = "ACCOUNT:LOCK:";

    /**
     * 运维端账号锁定
     */
    public static final String ADMIN_ACCOUNT_LOCK = "ACCOUNT:LOCK:ADMIN:";

    /**
     * 短信验证码
     */
    public static final String MESSAGE_SECURITY_CODE = "MESSAGE:SECURITY:CODE:";

    /**
     * 验证操作
     */
    public static final String CHECK_CODE = "CHECK:CODE:";

    /**
     * 人员TOKEN
     */
    public static final String WEB_ACCOUNT_TOKEN = "TOKEN:CACHE:";

    /**
     * 人员TOKEN 运维
     */
    public static final String ADMIN_ACCOUNT_TOKEN = "TOKEN:CACHE:ADMIN:";
    /**
     * AccessToken 储存
     */
    public static final String XCXPAERNTACCESSTOKEN = "accessToken:xcx";

    public static final String GZHACCESSTOKEN = "accessToken:gzh";

    /**
     * 人员菜单权限
     */
    public static final String ROLE_MENU = "ROLE:MENU:";

    /**
     * 人员菜单权限 运维端
     */
    public static final String ROLE_MENU_ADMIN = "ROLE:MENU:ADMIN:";

    /**
     * 机构层级树
     */
    public static final String ORG_TREE_CACHE = "ORGTREE";

    /**
     * 区域层级树
     */
    public static final String AREA_TREE_CACHE = "AREATREE";
    public static final String YIMATONG_TOKEN = "YIMATONG:TOKEN";
    public static final String RECEVICEC0DE_DEVICEID_RECEIVESENDTIME = " RECEVICEC0DE:";
    /**
     * TOKEN默认过期时间 1周
     */
    public static long TOKEN_EXPIRE_WEEK = 604800;
    /**
     * 普通缓存过期时间 1天
     */
    public static long NORMAL_EXPIRE_DAY = 86400;
    public static long HOUR_EXPIRE_DAY = 3600;
    public static long TOW_HOUR_EXPIRE_DAY = 7200;
    public static long THIRTY_EXPIRE = 3600;
    /**
     * 30分钟过期
     */
    public static long THIRTY_MINUTE_EXPIRE = 1800;
    public static long THIRTY_DAY_EXPIRE = 2592000;
    /**
     * 普通缓存过期时间 10小时
     */
    public static long NORMAL_EXPIRE_TEN_HOURS = 36000;

    /**
     * 普通缓存过期时间 10分钟
     */
    public static long NORMAL_EXPIRE_TEN_MINUTE = 600;

    /**
     * 普通缓存过期时间 1分钟
     */
    public static long NORMAL_EXPIRE_ONE_MINUTE = 60;
    /**
     * 角色列表
     */
    public static final String ROLE_LIST = "ROLE:LIST";


    /**
     * 角色列表
     */
    public static final String ROLE_LIST_ADMIN = "ROLE:LIST:ADMIN";
    /**
     * 接口权限
     */
    public static final String ROLE_SIGN = "SIGN:ROLE:";

    /**
     * 数据权限
     */
    public static final String PERSON_DATA_CHILD_PARENT = "PERSON:DATA:CHILDANDPARENT:";

    /**
     * 数据权限
     */
    public static final String PERSON_DATA_CHILD_PARENT_ADMIN = "PERSON:DATA:CHILDANDPARENT:ADMIN:";
    public static final String PERSON_DATA_STAFF = "PERSON:DATA:STAFF:";
    public static final String PERSON_DATA_DEVICE = "PERSON:DATA:DEVICE:";

    public static final String PERSON_DATA_STAFF_ADMIN = "PERSON:DATA:STAFF:ADMIN:";

    public static final String PERSON_DATA_DEVICE_ADMIN = "PERSON:DATA:DEVICE:ADMIN:";
    /**
     * 设备信息
     */
    public static final String RABBIT_DEVICE = "RABBIT:DEVICE:";
    /**
     * 设备心跳
     */
    public static final String RABBIT_DEVICE_HEART = "RABBIT:DEVICE:HEART:";


    /**
     * 短信验证码
     */
    public static final String SMS_CHECK_CODE_SUCCESS = "SMS:CHECK:CODE:SUCCESS:";

    /**
     * 身份证解析
     */
    public static final String ACCESS_TOKEN_IDCARD = "TOKEN:ACCESS:IDCARD:";


}
