package com.jmtc.file2chain.api;

import com.jmtc.file2chain.domain.result.NotificationMsg;
import com.jmtc.file2chain.domain.result.RespEntity;
import com.jmtc.file2chain.domain.result.Response;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Chris
 * @date 2021/6/1 21:00
 * @Email:gang.wu@nexgaming.com
 */
public class BaseController {
    protected Logger logger =  LoggerFactory.getLogger(this.getClass());

    static Map<Integer, Date> apiLimitMap = new ConcurrentHashMap<>();

    protected Response result(NotificationMsg msg){
        return new Response(msg);
    }
    protected Response result(){
        return new Response();
    }

    protected HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    protected String getUserIp() {
        String value = getRequest().getHeader("X-Real-IP");
        if (StringUtils.isNotBlank(value) && !"unknown".equalsIgnoreCase(value)) {
            return value;
        } else {
            return getRequest().getRemoteAddr();
        }
    }

    public RespEntity<Map<String, Object>> error(NotificationMsg notificationMsg) {
        return new RespEntity<Map<String, Object>>(notificationMsg.getCode() , notificationMsg.getMsg());
    }

    public RespEntity<Map<String, Object>> success(Map<String, Object> map) {
        return new RespEntity<Map<String, Object>>(NotificationMsg.SUCCESS.getCode(), NotificationMsg.SUCCESS.getMsg(), map);
    }

    public RespEntity<Map<String, Object>> success() {
        return new RespEntity<Map<String, Object>>(NotificationMsg.SUCCESS.getCode(), NotificationMsg.SUCCESS.getMsg());
    }
}
