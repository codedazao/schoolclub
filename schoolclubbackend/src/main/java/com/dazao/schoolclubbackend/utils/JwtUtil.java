package com.dazao.schoolclubbackend.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dazao.schoolclubbackend.entity.security.MyUserDetails;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {
    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Value("${spring.security.key}")
    String key;
    @Value("${spring.security.expire}")
    int expire;

    //登出接口专用的验证的方法
    public boolean invalidateJwt(String headerToken) {
        String jwtString = this.getJwtString(headerToken);
        if (jwtString == null) return false;
        Algorithm algorithm = Algorithm.HMAC256(key);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        try {
            DecodedJWT decodedJWT = jwtVerifier.verify(jwtString);
            String jwtId = decodedJWT.getId();
            return deleteToken(jwtId, decodedJWT.getExpiresAt());
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    //token存入黑名单
    private boolean deleteToken(String uuid, Date time) {
        //time是现在传入的时间（现在的时间）
        //token没在redis中
        if (!this.isInvalidToken(uuid)){
            Date now = new Date();
            long expireTime = time.getTime() - now.getTime();
            long timeForRedis = Math.max(expireTime, 0);
            stringRedisTemplate.opsForValue().set(Const.JWT_BLACK_LIST + uuid,
                    "",
                    timeForRedis,
                    TimeUnit.MILLISECONDS);
            System.out.println(stringRedisTemplate.opsForValue().get(Const.JWT_BLACK_LIST + uuid));
            return true;
        }else {
            return false;
        }
    }

    private boolean isInvalidToken(String uuid){
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(Const.JWT_BLACK_LIST + uuid));
    }

    //解析jwt
    public DecodedJWT resolveJwt(String headerToken) {
        String stringJwt;
        if ((stringJwt = this.getJwtString(headerToken)) == null)
            return null;
        Algorithm algorithm = Algorithm.HMAC256(key);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        try {
            DecodedJWT verify = jwtVerifier.verify(stringJwt);
            //看一看token有没有被拉黑
            if (this.isInvalidToken(verify.getId())) {
                return null;
            }
            Date expireTime = verify.getExpiresAt();
            return new Date().after(expireTime) ? null : verify;
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    //转换user
    public UserDetails toUser(DecodedJWT jwt) {
        Map<String, Claim> claims = jwt.getClaims();
        return new MyUserDetails()
                .setUsername(claims.get("name").asString())
                .setUserId(claims.get("id").asInt())
                .setPassword("********")
                .setAuthorities(claims.get("authorities").asArray(String.class));
    }

    private String getJwtString(String headerToken) {
        if (headerToken == null || !headerToken.startsWith("Baerer ")) {
            return null;
        }
        return headerToken.substring(7);
    }

    public String createJwt(UserDetails userDetails, int id, String username) {
        Algorithm algorithm = Algorithm.HMAC256(key);
        return JWT.create()
                .withJWTId(UUID.randomUUID().toString())
                .withClaim("id", id)
                .withClaim("name", username)
                .withClaim("authorities", userDetails
                        .getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .withExpiresAt(expireTime())
                .sign(algorithm);
    }

    public Date expireTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, expire * 24);
        return calendar.getTime();
    }
}
