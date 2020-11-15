package org.ywb.aoplimiter.support.defaults;

import lombok.NoArgsConstructor;
import org.ywb.aoplimiter.support.LimitProperties;

import java.util.concurrent.TimeUnit;

/**
 * @author yuwenbo1
 * @date 2020/11/15 5:53 下午 星期日
 * @since 1.0.0
 */
@NoArgsConstructor
public class DefaultLimitProperties implements LimitProperties {

    private  int replenishRate;

    private  int burstCapacity;

    private  TimeUnit timeUnit;

    public DefaultLimitProperties(int replenishRate, int burstCapacity, TimeUnit timeUnit) {
        this.replenishRate = replenishRate;
        this.burstCapacity = burstCapacity;
        this.timeUnit = timeUnit;
    }

    @Override
    public int replenishRate() {
        return replenishRate;
    }

    @Override
    public int burstCapacity() {
        return burstCapacity;
    }

    @Override
    public TimeUnit timeUnit() {
        return timeUnit;
    }
}
