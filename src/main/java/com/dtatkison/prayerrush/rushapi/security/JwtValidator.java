package com.dtatkison.prayerrush.rushapi.security;

import com.dtatkison.prayerrush.rushapi.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

@Component
public class JwtValidator {
    private String secret = "prayerrush";

    public User validate(String token) {

        User jwtUser = null;
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

            jwtUser = new User();

            jwtUser.setUsername(body.getSubject());
            jwtUser.setId(Integer.parseInt((String) body.get("userId")));
        }
        catch (Exception e) {
            System.out.println(e);
        }

        return jwtUser;
    }
}
