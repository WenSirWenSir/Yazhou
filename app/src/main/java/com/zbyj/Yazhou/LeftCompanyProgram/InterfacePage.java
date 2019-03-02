package com.zbyj.Yazhou.LeftCompanyProgram;

public interface InterfacePage {
    void onSucess(String tOrgin);//成功的监听
    void onNotConnect();//网络断开连接
    void onFail(String tOrgin);//失败的监听

}
