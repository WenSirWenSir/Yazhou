package com.zbyj.Yazhou.LeftCompanyProgram;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * json数据解析
 */
public class JsonEndata {
    private String mJsonString;

    public JsonEndata(String json) {
        this.mJsonString = json;

    }

    /**
     * 获取JSON中的一个指定的KEY的值
     */
    public String getJsonKeyValue(String key) {
        String keyValue = "";
        try {
            JSONObject jsonObject = new JSONObject(this.mJsonString);
            keyValue = jsonObject.getString(key);
        } catch (JSONException e) {
            Log.e("capitalist", "json解析错误:" + e.getMessage());
            return "";
        }
        return keyValue;
    }
}
