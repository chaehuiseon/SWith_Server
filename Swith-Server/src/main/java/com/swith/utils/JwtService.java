package com.swith.utils;


import com.swith.config.BaseException;
import com.swith.config.BaseResponseStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class JwtService {

    @Value("${secret.jwt-secret-key}")
    String JWT_SECRET_KEY;

    /**
     * JWT 생성
     *
     * @param userIdx
     * @return String
     */
    public String createJwt(int userIdx) {
        Date now = new Date();
        System.out.println("jwt key :"+ JWT_SECRET_KEY);
        return Jwts.builder()
                .setHeaderParam("type", "jwt")
                .claim("userIdx", userIdx)
                .setIssuedAt(now)
//                .signWith(SignatureAlgorithm.HS256, Secret.JWT_SECRET_KEY)
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY)
                .setExpiration(new Date(System.currentTimeMillis() + 1 * (1000 * 60 * 60 * 24 * 365))) //JWT 발급 만료기간
                .compact();
    }

    /**
     * Header에서 X-ACCESS-TOKEN으로 JWT 추출
     *
     * @return String
     */
    public String getJwt() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("X-ACCESS-TOKEN");
    }

    /**
     * JWT에서 userIdx 추출
     *
     * @return int
     * @throws BaseException
     */
    public int getUserIdx() throws BaseException {
        // 1. JWT 추출
        String accessToken = getJwt();
        if (accessToken == null || accessToken.length() == 0) {
            throw new BaseException(BaseResponseStatus.EMPTY_JWT);
        }

        // 2. JWT parsing
        Jws<Claims> claims;
        try {
            claims = Jwts.parser()
//                    .setSigningKey(Secret.JWT_SECRET_KEY)
                    .setSigningKey(JWT_SECRET_KEY)
                    .parseClaimsJws(accessToken);
        } catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.INVALID_JWT);
        }

        // 3. userIdx 추출
        return claims.getBody().get("userIdx", Integer.class);  // jwt에서 userIdx 추출
    }

}