package com.dtatkison.prayerrush.rushapi.security;

import com.dtatkison.prayerrush.rushapi.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

@Component
public class JwtGenerator {
    public String generate(User user)
    {
        Claims claims = Jwts.claims()
                .setSubject(user.getEmail());
        claims.put("userId", String.valueOf(user.getId()));
        claims.put("email", user.getEmail());
        claims.put("firstname", user.getFirstname());
        claims.put("lastname", user.getLastname());

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, "prayerrush")
                .compact();
    }
}
