package com.jmtc.file2chain.utils;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Chris
 * @date 2021/6/1 21:16
 * @Email:gang.wu@nexgaming.com
 */
public class Maps {
    private Map<String, Object> params;

    private Maps() {
        this.params = new HashMap();
    }

    public Maps add(String key, Object value) {
        if (value == null) {
            this.params.remove(key);
        } else {
            if (!StringUtils.isEmpty(key)) {
                this.params.put(key, value);
            }
        }
        return this;
    }

    public Maps add(Map<String, Object> values) {
        if (values != null) {
            for (Map.Entry<String, Object> entry : values.entrySet()) {
                add(entry.getKey(), entry.getValue());
            }
        }
        return this;
    }

    public Maps remove(String key) {
        if (!StringUtils.isEmpty(key)) {
            this.params.remove(key);
        }
        return this;
    }

    public Map<String, Object> get() {
        return this.params;
    }

    public static Maps create() {
        Maps builder = new Maps();
        return builder;
    }

    public static Maps create(String key, Object value) {
        Maps builder = new Maps();
        builder.add(key, value);
        return builder;
    }

    public static Maps create(Map<String, Object> values) {
        Maps builder = new Maps();
        builder.add(values);
        return builder;
    }
}
