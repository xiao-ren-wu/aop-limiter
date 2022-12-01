package org.ywb.aoplimiter.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.util.Assert;
import org.ywb.aoplimiter.anns.RateLimit;
import org.ywb.aoplimiter.exception.RateLimitException;
import org.ywb.aoplimiter.support.HttpContentHelper;
import org.ywb.aoplimiter.support.KeyResolver;
import org.ywb.aoplimiter.support.LimitProperties;
import org.ywb.aoplimiter.support.defaults.DefaultLimitProperties;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author yuwenbo1
 * @date 2020/11/15 5:55 下午 星期日
 * @since 1.0.0
 */
@Slf4j
@Aspect
public class RateLimitInterceptor implements ApplicationContextAware {

    @Resource
    private RedisTemplate<String, Object> stringRedisTemplate;

    @Resource
    private RedisScript<List<Long>> rateLimitRedisScript;

    private ApplicationContext applicationContext;

    @Around("execution(public * *(..)) && @annotation(org.ywb.aoplimiter.anns.RateLimit)")
    public Object interceptor(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        RateLimit rateLimit = method.getAnnotation(RateLimit.class);
        // 断言不会被限流
        assertNonLimit(rateLimit, pjp);
        return pjp.proceed();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void assertNonLimit(RateLimit rateLimit, ProceedingJoinPoint pjp) {
        Class<? extends KeyResolver> keyResolverClazz = rateLimit.keyResolver();
        KeyResolver keyResolver = applicationContext.getBean(keyResolverClazz);
        String resolve = keyResolver.resolve(HttpContentHelper.getCurrentRequest(), pjp);
        List<String> keys = getKeys(resolve);

        LimitProperties limitProperties = getLimitProperties(rateLimit);

        // 根据限流时间维度计算时间
        long timeLong = getCurrentTimeLong(limitProperties.timeUnit());

        // The arguments to the LUA script. time() returns unixTime in seconds.
        List<String> scriptArgs = Arrays.asList(limitProperties.replenishRate() + "",
                limitProperties.burstCapacity() + "",
                (Instant.now().toEpochMilli() / timeLong) + "",
                "1", timeLong + "");

        // 第一个参数是是否被限流，第二个参数是剩余令牌数
        List<Long> rateLimitResponse = this.stringRedisTemplate.execute(this.rateLimitRedisScript, keys, scriptArgs.toArray());
        Assert.notNull(rateLimitResponse, "redis execute redis lua limit failed.");
        Long isAllowed = rateLimitResponse.get(0);
        Long newTokens = rateLimitResponse.get(1);
        log.info("rate limit key [{}] result: isAllowed [{}] new tokens [{}].", resolve, isAllowed, newTokens);
        if (isAllowed <= 0) {
            throw new RateLimitException(resolve);
        }
    }

    private LimitProperties getLimitProperties(RateLimit rateLimit) {
        Class<? extends LimitProperties> aClass = rateLimit.limitProperties();
        if (aClass == DefaultLimitProperties.class) {
            // 选取注解中的配置
            return new DefaultLimitProperties(rateLimit.replenishRate(), rateLimit.burstCapacity(), rateLimit.timeUnit());
        }
        // 优先使用用户自己的配置类
        return applicationContext.getBean(aClass);
    }

    private long getCurrentTimeLong(TimeUnit timeUnit) {
        switch (timeUnit) {
            case SECONDS:
                return 1000;
            case MINUTES:
                return 1000 * 60;
            case HOURS:
                return 1000 * 60 * 60;
            case DAYS:
                return 1000 * 60 * 60 * 24;
            default:
                throw new IllegalArgumentException("timeUnit:" + timeUnit + " not support");
        }
    }

    private List<String> getKeys(String id) {
        // use `{}` around keys to use Redis Key hash tags
        // this allows for using redis cluster

        // Make a unique key per user.
        String prefix = "request_rate_limiter.{" + id;

        // You need two Redis keys for Token Bucket.
        String tokenKey = prefix + "}.tokens";
        String timestampKey = prefix + "}.timestamp";
        return Arrays.asList(tokenKey, timestampKey);
    }
}