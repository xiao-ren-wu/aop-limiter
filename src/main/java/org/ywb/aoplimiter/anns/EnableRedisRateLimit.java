package org.ywb.aoplimiter.anns;

import org.springframework.context.annotation.Import;
import org.ywb.aoplimiter.config.RateRateLimitConfiguration;

import java.lang.annotation.*;

/**
 * @author yuwenbo1
 * @date 2020/11/15 5:58 下午 星期日
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({RateRateLimitConfiguration.class})
public @interface EnableRedisRateLimit {

}
