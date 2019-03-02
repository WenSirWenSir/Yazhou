package com.zbyj.Yazhou.Interface;

import com.zbyj.Yazhou.LeftCompanyProgram.JsonEndata;

/**
 * 用户的监听集合
 */
public class UserInterface {
    public static interface userCheckVerificationOndone {
        /**
         * 系统处理完用户提交的验证码和手机信息
         */
        void ondone(JsonEndata jsonEndata);
    }
}




