package org.ywb.aoplimiter.support;

import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yuwenbo1
 * @date 2020/11/15 5:51 下午 星期日
 * @since 1.0.0
 * 根据请求的uri进行限流
 */
public class UriKeyResolver implements KeyResolver {

    @Override
    public String resolve(HttpServletRequest request, ProceedingJoinPoint pjp) {
        return request.getRequestURI();
    }
}
