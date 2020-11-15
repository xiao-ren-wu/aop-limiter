package org.ywb.aoplimiter.exception;

/**
 * @author yuwenbo1
 * @date 2020/11/15 5:48 下午 星期日
 * @since 1.0.0
 */
public class RateLimitException extends RuntimeException {
    public RateLimitException(String message) {
        super(message);
    }
}