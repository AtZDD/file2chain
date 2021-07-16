package com.jmtc.file2chain.domain.result;

/**
 * @author Chris
 * @date 2021/6/1 21:02
 * @Email:gang.wu@nexgaming.com
 */
public enum NotificationMsg {
    SUCCESS("000000", "Operation Success"),
    FAILED("999999","Operation Fail"),
    LOG_ADD_FAIL("000001", "add log info failed"),
    LOG_FIND_FAIL("000002", "find log info failed"),
            ;

    private NotificationMsg(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    private String code;
    private String msg;

    public String getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }
}
