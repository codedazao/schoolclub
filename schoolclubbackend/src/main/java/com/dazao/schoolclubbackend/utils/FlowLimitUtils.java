package com.dazao.schoolclubbackend.utils;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class FlowLimitUtils {
    @Resource
    StringRedisTemplate stringRedisTemplate;


    /**
     * @param limitKey  用户限流的key
     * @param blockTime 限流时间
     * @return 返回是否可以凡哥维纳接口
     */
    public boolean limitOnceCheck(String limitKey, int blockTime) {
        synchronized (limitKey.intern()) {
            if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(Const.VERIFY_EMAIL_LIMIT+limitKey))) {
                return false;
            } else {
                stringRedisTemplate.opsForValue().set(new StringBuilder()
                                .append(Const.VERIFY_EMAIL_LIMIT)
                                .append(limitKey).toString(),
                        "",
                        blockTime,
                        TimeUnit.SECONDS);
                return true;
            }
        }
    }
}
