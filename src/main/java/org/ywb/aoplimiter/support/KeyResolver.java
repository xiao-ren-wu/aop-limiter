package org.ywb.aoplimiter.support;

import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yuwenbo1
 * @date 2020/11/15 5:50 下午 星期日
 * @since 1.0.0
 */
public interface KeyResolver {
    /**
     * 具体限流规则
     *
     * @param request request
     * @param pjp
     * @return request
     */
    String resolve(HttpServletRequest request, ProceedingJoinPoint pjp);
}