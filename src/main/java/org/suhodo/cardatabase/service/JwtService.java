package org.suhodo.cardatabase.service;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class JwtService {

    static final long EXPIRATIONTIME = 86400000; // 토큰 유효기간, 1일 ms로 계산(24*60*60*1000)
    static final String PREFIX = "Bearer";       // jwt토큰의 접두어
    static final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // 비밀키 생성

    // 비밀키로 서명된 token 발급
    // username, 유효기간, 비밀키
    public String getToken(String username){
        String token = Jwts.builder()
                        .setSubject(username)
                        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                        .signWith(key)
                        .compact();
        return token;
    }

    // 브라우저->tomcat->HttpServletRequest 요청의 Header에 있는 token를 꺼내서 token 검증
    // token에서 username을 추출
    public String getAuthUser(HttpServletRequest request){
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(token != null){
            String username = Jwts.parserBuilder()
                            .setSigningKey(key)
                            .build()
                            .parseClaimsJws(token.replace(PREFIX, ""))
                            .getBody()
                            .getSubject();

            if(username != null)
                return username;
        }
        return null;
    }
}
