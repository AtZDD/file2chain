package com.jmtc.file2chain.comm;

/**
 * @author Chris
 * @date 2021/6/7 7:35
 * @Email:gang.wu@nexgaming.com
 */
public class TmException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String msg;
    private int code = 500;

    public TmException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public TmException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public TmException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public TmException(String msg, int code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


}
