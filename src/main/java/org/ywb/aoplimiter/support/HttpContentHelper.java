package org.ywb.aoplimiter.support;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author yuwenbo1
 * @date 2020/11/15 5:57 下午 星期日
 * @since 1.0.0
 */
public class HttpContentHelper {

    /**
     * 获取当前请求的Request对象
     */
    public static HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes == null ? null : requestAttributes.getRequest();
    }

    /**
     * 获取请求的header
     *
     * @param name name
     * @return String
     */
    public static String getHeader(String name) {
        HttpServletRequest currentRequest = getCurrentRequest();
        return Objects.isNull(currentRequest) ? null : currentRequest.getHeader(name);
    }

    /**
     * 获取当前请求的Response对象
     */
    public static HttpServletResponse getCurrentResponse() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes == null ? null : requestAttributes.getResponse();
    }


    /**
     * 获取远程IP
     *
     * @return string
     */
    public static String getRemoteAddress() {
        HttpServletRequest request = getCurrentRequest();
        if (Objects.isNull(request)) {
            return "";
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getRemoteAddr();
        }
        if (ip == null) {
            return "";
        } else {
            return ip.split(",")[0];
        }
    }

    /**
     * 获取所有请求的值
     */
    public static Map<String, String> getRequestParameters() {
        HashMap<String, String> values = new HashMap<>();
        HttpServletRequest request = getCurrentRequest();
        if (request == null) {
            return values;
        }
        Enumeration<String> enums = request.getParameterNames();
        while (enums.hasMoreElements()) {
            String paramName = (String) enums.nextElement();
            String paramValue = request.getParameter(paramName);
            values.put(paramName, paramValue);
        }
        return values;
    }
}
