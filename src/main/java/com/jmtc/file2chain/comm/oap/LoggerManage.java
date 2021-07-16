package com.jmtc.file2chain.comm.oap;

import java.lang.annotation.*;

/**
 * @author Chris
 * @date 2021/6/1 21:12
 * @Email:gang.wu@nexgaming.com
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoggerManage {
    public String description();
}