package com.jmtc.file2chain.domain.result;

import java.io.Serializable;

/**
 * @author Chris
 * @date 2021/6/1 21:03
 * @Email:gang.wu@nexgaming.com
 */
public class RespEntity<T> implements Serializable {
    private static final long serialVersionUID = 7739979424806069482L;
    private String rspCode;
    private String rspMsg;
    private T rspData;

    public RespEntity() {
        this.rspCode = "";
        this.rspMsg = "";
    }

    public RespEntity(String code) {
        this.rspCode = (code == null ? "" : code);
        this.rspMsg = "";
    }

    public RespEntity(String code, String infor) {
        this.rspCode = (code == null ? "" : code);
        this.rspMsg = (infor == null ? "" : infor);
    }

    public RespEntity(String code, String infor, T cust) {
        this.rspCode = (code == null ? "" : code);
        this.rspMsg = (infor == null ? "" : infor);
        this.rspData = cust;
    }

    public String getRspCode() {
        return rspCode;
    }

    public String getRspMsg() {
        return rspMsg;
    }

    public T getRspData() {
        return rspData;
    }

    public void setRspData(T data) {
        this.rspData = data;
    }

    public void setRspCode(String rspCode) {
        this.rspCode = rspCode;
    }

    public void setRspMsg(String rspMsg) {
        this.rspMsg = rspMsg;
    }
}
