package com.zbyj.Yazhou;


/**
 * 公有变量集合  押粥
 */
public class config {

    public static final String DEBUG_STR = "Yazhou";
    public static final String SERVICE = "http://120.79.63.36";

    /**
     * 界面传值的value
     */
    public static final String ACTIVITY_ACTION_PHONE = "phone";
    /**
     * 应用程序是否更新
     */
    public static final int UPDATE_STATIC_ON = 0;//不用更新
    public static final int UPDATE_STATIC_OFF = 1;//要更新
    public static final int CALC_RESERVE_TIME = 0;//计算配送所需的时间倒计时
    public static final int CALC_PRICE_TIME = 1;
    /**
     * 网络访问提交方式
     */
    public static final int INTER_METHOD_GET = 0;
    public static final int INTER_METHOD_POST = 1;
    /**
     * 登录状态
     */
    public static final int LOGIN_DONE = 0;
    public static final int LOGIN_UP = 1;
    public static final int LOGIN_ERROR = 2;
    public static final int LOGIN_REFUSE = 3;


    /**
     * 获取屏幕的高度或者是宽度
     */
    public static final int GET_WINDOW_HEIGHT = 0;
    public static final int GET_WINDOW_WIDTH = 1;

    /**
     * 获取系统的内存大小的标注方式
     */

    public static final int GET_SYSTEM_MEMORYSIZE_MB = 1;//获取格式为MB
    public static final int GET_SYSTEM_MEMORYSIZE_KB = 0;//获取格式为KB
    /**
     * 服务器的地址
     */
    public static final String SERVICE_ADDR = "http://120.79.63.36/YazhouService";

    /**
     * 网络访问返回的数据信息
     */
    public static final String WEB_SERVICE_PROGRAM_VERSION = "programVersion";//显示程序的版本
    public static final String WEB_SERVICE_PROGRAM_SHOWIMG = "showimg";//是否要显示首页展示的图片
    public static final String WEB_SERVICE_PROGRAM_IMG = "img";//IMG的地址


    /**
     * 网络访问action
     */
    public static final String WEB_SERVICE_ACTION_GETRESERVETIME = "0";//获取配送所需时间
    public static final String WEB_SERVICE_SEND_MESSAGESTATUS = "return_code";//获取短信发送的状态
    public static final String WEB_SERVICE_KEY_ACTION = "action" ;//指令


    /**
     * 网络访问到的数据的的KEY的集合
     */


    /**
     * 用户登录action/和状态
     */

    public static final String USER_LOGIN_CHECK_CODE_PHONE = "phone";
    public static final String USER_LOGIN_CHECK_CODE = "code";
    public static final String USER_LOGIN_SUCESS = "0";//登录成功
    public static final String USER_LOGIN_FAIL = "1";//登录失败
    public static final String USER_LOGIN_VERIFICATION_ERROR = "2";//验证码错误
    public static final String USER_LOGIN_CHECK_STATUS = "status";
    public static final String USER_LOGIN_CHECK_MODULE = "0";//要用到的工厂标识


    /**
     * 发送短信状态
     */
    public static final String SEND_MESSAGE_OK = "00000";//成功

    /**
     * 读取本地用户保存的记录
     */
    public static final String SAVE_LOCAL_USERPHONE ="phone" ;
    public static final String SAVE_LOCAL_USERTOKEN = "token" ;

    /**
     * 获取用户模块的地址
     *
     * @return
     */
    public static String getServiceUsertoolsAddr() {
        return SERVICE_ADDR + "/tools/USER_TOOLS.php";
    }

    /**
     * 获取首页配置的信息地址
     */

    public static String getServiceProgramMainConfig() {
        return SERVICE_ADDR + "/tools/PROGRAM_MAIN.php";
    }


    /**
     * 获取发送验证码的地址
     *
     * @return
     */
    public static String getSendServiceAddr() {
        return SERVICE_ADDR + "/tools/SEND_VERIFICATION.php";
    }

    /**
     * 取得服务器中返回的配送所需要的时间
     *
     * @return
     */
    public static String getReserveTime() {
        return SERVICE_ADDR + "/tools/TIME_TOOLS.php";
    }

    /**
     * 发送短信验证码的服务器地址
     */

    public static String getSendVerificationAddr() {
        return SERVICE_ADDR + "/tools/SEND_VERIFICATION.php";
    }

    /**
     * 获取用户的网络服务工厂地址
     */

    public static String getUsermoduleIntentFactory() {
        return SERVICE_ADDR + "/tools/USER_TOOLS.php";
    }
}
