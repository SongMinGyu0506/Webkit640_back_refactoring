package com.webkit640.backend.config.security;

import com.webkit640.backend.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Configuration
public class TokenProviderImpl implements TokenProvider{

    @Value("${jwt-key}")
    private String JWT_KEY;

    @Override
    public int validateAndGetUserId(String token) {
        Claims claims = Jwts.parser().setSigningKey(JWT_KEY).parseClaimsJws(token).getBody();
        return Integer.parseInt(claims.getSubject());
    }

    @Override
    public String create(Member member) {
        Date expireDate = Date.from(Instant.now().plus(1, ChronoUnit.HOURS));
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512,JWT_KEY)
                .setSubject(Integer.toString(member.getId())).setIssuer("WEBKIT640SERVER").setIssuedAt(new Date())
                .setExpiration(expireDate).compact();
    }
}
