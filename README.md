# aop-limiter
本地对该项目执行install
## 你的项目引入下面的坐标
~~~xml
        <dependency>
           <groupId>org.ywb</groupId>
           <artifactId>aop-limiter</artifactId>
           <version>0.0.1-SNAPSHOT</version>
        </dependency>
~~~
## 手动装配限流
启动类或者配置类标准`@EnableRedisRateLimit`
## 需要限流的uri接口标注`@RateLimit`
1. `replenishRate`单位时间填充速率，`burstCapacity`令牌桶容量，`timeUnit`时间单位
2. 默认使用uri作为依据进行限流，如果需要变更，可以通过实现`KeyResolver`编写自己的限流依据即可。
3. 默认使用注解中的参数作为限流参数，如果需要使用配置类的形式对限流参数进行指定，可以通过实现`LimitProperties`接口
