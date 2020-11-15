package org.ywb.aoplimiter.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.ywb.aoplimiter.interceptor.RateLimitInterceptor;
import org.ywb.aoplimiter.support.KeyResolver;
import org.ywb.aoplimiter.support.UriKeyResolver;

import java.util.List;

/**
 * @author yuwenbo1
 * @date 2020/11/15 5:54 下午 星期日
 * @since 1.0.0
 */
@Slf4j
public class RateRateLimitConfiguration {

    public RateRateLimitConfiguration() {
        log.info("initialize redis rate limit...");
    }

    @Bean(name = "rateLimitRedisScript")
    public RedisScript<List<Long>> rateLimitRedisScript() {
        DefaultRedisScript redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("META-INF/scripts/redis_rate_limiter.lua")));
        redisScript.setResultType(List.class);
        return redisScript;
    }

    @Bean
    public RateLimitInterceptor rateLimitInterceptor() {
        return new RateLimitInterceptor();
    }

    @Bean
    public KeyResolver keyResolver() {
        return new UriKeyResolver();
    }

}
