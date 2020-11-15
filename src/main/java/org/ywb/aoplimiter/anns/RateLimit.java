package org.ywb.aoplimiter.anns;

import org.ywb.aoplimiter.support.KeyResolver;
import org.ywb.aoplimiter.support.LimitProperties;
import org.ywb.aoplimiter.support.UriKeyResolver;
import org.ywb.aoplimiter.support.defaults.DefaultLimitProperties;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author yuwenbo1
 * @date 2020/11/15 5:49 下午 星期日
 * @since 1.0.0
 */
@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {

    /**
     * 限流维度，默认使用uri进行限流
     *
     * @return uri
     */
    Class<? extends KeyResolver> keyResolver() default UriKeyResolver.class;

    /**
     * 限流配置，如果实现了该接口，默认以这个为准
     *
     * @return limitProp
     */
    Class<? extends LimitProperties> limitProperties() default DefaultLimitProperties.class;

    /**
     * 令牌桶每秒填充平均速率
     *
     * @return replenishRate
     */
    int replenishRate() default 1;

    /**
     * 令牌桶总容量
     *
     * @return burstCapacity
     */
    int burstCapacity() default 3;

    /**
     * 限流时间维度，默认为秒
     * 支持秒，分钟，小时，天
     * 即，
     * {@link TimeUnit#SECONDS},
     * {@link TimeUnit#MINUTES},
     * {@link TimeUnit#HOURS},
     * {@link TimeUnit#DAYS}
     *
     * @return TimeUnit
     * @since 1.0.2
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}

