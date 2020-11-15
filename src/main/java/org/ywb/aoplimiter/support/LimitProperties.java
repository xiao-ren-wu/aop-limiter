package org.ywb.aoplimiter.support;

import java.util.concurrent.TimeUnit;

/**
 * @author yuwenbo1
 * @date 2020/11/15 5:52 下午 星期日
 * @since 1.0.0
 */
public interface LimitProperties {
    /**
     * 令牌桶每秒填充平均速率
     *
     * @return replenishRate
     */
    int replenishRate();

    /**
     * 令牌桶总容量
     *
     * @return burstCapacity
     */
    int burstCapacity();

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
    TimeUnit timeUnit();
}

