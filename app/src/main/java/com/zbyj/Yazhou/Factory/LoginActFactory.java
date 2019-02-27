package com.zbyj.Yazhou.Factory;


import android.content.Context;
import android.util.Log;

import com.zbyj.Yazhou.ConNet.SystemVisitInterService;
import com.zbyj.Yazhou.ConfigPageValue.USER_KEY_PAGE;
import com.zbyj.Yazhou.Interface.UserInterface;
import com.zbyj.Yazhou.Utils.JsonEndata;
import com.zbyj.Yazhou.config;
import com.zbyj.Yazhou.tools;

/**
 * 登录用到的工厂类
 */
public class LoginActFactory {
    /**
     * 判断输入的验证码是否正确
     *
     * @param tContext      上下文
     * @param tPhone        验证的登录电话
     * @param tCode         验证的登录的手机号码
     * @param userInterface
     * @return
     */
    public static void isCodecorrect(final Context tContext,final String tPhone, String tCode, final UserInterface.userCheckVerificationOndone userInterface) {
        //MD5加密  在网络中 不要用明文传输数据信息
        SystemVisitInterService.InterServiceGet(tContext, config.getUsermoduleIntentFactory(), new SystemVisitInterService.onVisitInterServiceListener() {
            @Override
            public void onSucess(String tOrgin) {
                //如果网络访问成功 就判断是否验证成功
                JsonEndata jsonEndata = new JsonEndata(tOrgin);
                Log.i(config.DEBUG_STR,"网络获取的数据为:" + tOrgin);
                String status = jsonEndata.getJsonKeyValue(config.USER_LOGIN_CHECK_STATUS);//获取登录状态
                Log.i(config.DEBUG_STR,"登录状态为：" + status);
                if(userInterface != null){
                    userInterface.ondone(jsonEndata);
                }

            }

            @Override
            public void onNotConnect() {

            }

            @Override
            public void onFail(String tOrgin) {

            }
        }, config.WEB_SERVICE_KEY_ACTION,config.USER_LOGIN_CHECK_MODULE,config.USER_LOGIN_CHECK_CODE_PHONE, tPhone, config.USER_LOGIN_CHECK_CODE, tCode);
    }


}
