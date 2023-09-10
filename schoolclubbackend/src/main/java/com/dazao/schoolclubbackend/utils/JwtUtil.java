package com.dazao.schoolclubbackend.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dazao.schoolclubbackend.entity.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {
    @Value("${spring.security.key}")
    String key;
    @Value("${spring.security.expire}")
    int expire;

    //解析jwt
    public DecodedJWT resolveJwt(String headerToken){
        String stringJwt;
        if ((stringJwt = this.getJwtString(headerToken)) == null)
            return null;
        Algorithm algorithm = Algorithm.HMAC256(key);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        try{
            DecodedJWT verify = jwtVerifier.verify(stringJwt);
            Date expireTime = verify.getExpiresAt();
            return new Date().after(expireTime) ? null: verify;
        }catch (JWTVerificationException e){
            return null;
        }
    }

    //转换user
    public UserDetails toUser(DecodedJWT jwt){
        Map<String, Claim> claims = jwt.getClaims();
        return new MyUserDetails()
                .setUsername(claims.get("name").asString())
                .setUserId(claims.get("id").asInt())
                .setPassword("********")
                .setAuthorities(claims.get("authorities").asArray(String.class));
    }

    private String getJwtString(String headerToken){
        if (!headerToken.startsWith("Baerer ") || headerToken == null) {
            return null;
        }
        return headerToken.substring(7);
    }

    public String createJwt(UserDetails userDetails, int id, String username){
        Algorithm algorithm = Algorithm.HMAC256(key);
        return JWT.create().withClaim("id",id)
                .withClaim("name",username)
                .withClaim("authorities",userDetails
                        .getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .withExpiresAt(expireTime())
                .sign(algorithm);
    }

    private Date expireTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR,expire * 24);
        return calendar.getTime();
    }
}
