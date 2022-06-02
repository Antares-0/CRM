package com.powernode.crm.commons.domain;

/**
 * @author LXM
 * @create 2022-04-25 13:50
 */
public class ReturnObject {
    private String code; // 1成功 0 失败
    private String message;
    private Object retData; // 其他数据

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getRetData() {
        return retData;
    }

    public void setRetData(Object retData) {
        this.retData = retData;
    }
}
