package com.hr.springboot.multi.tenant.app.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Date;

public class AuthenticationService {

    private static final long EXPIRATION_TIME = 864_000_00;
    private static final String JWT_SIGNING_KEY = "my-superrrr-secret-key";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER = "Bearer ";

    public static void addToken(HttpServletResponse response, String userName, String tenant)
    {
        final String jwtToken = Jwts.builder()
                .setSubject(userName)
                .setAudience(tenant)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, JWT_SIGNING_KEY)
                .compact();

        response.addHeader(AUTHORIZATION_HEADER,BEARER+jwtToken);
    }

    public static Authentication getAuthentication(HttpServletRequest request)
    {
        final String token = request.getHeader(AUTHORIZATION_HEADER);
        if(null!=token)
        {
            final String userName = Jwts.parser()
                    .setSigningKey(JWT_SIGNING_KEY)
                    .parseClaimsJws(token.replace(BEARER,""))
                    .getBody()
                    .getSubject();

            if(null!=userName)
            {
                return new UsernamePasswordAuthenticationToken(userName,null, Collections.emptyList());
            }
        }

        return null;
    }

    public static String getTenant(final HttpServletRequest request)
    {
        final String token = request.getHeader(AUTHORIZATION_HEADER);
        if(null!=token)
        {
            return Jwts
                    .parser()
                    .setSigningKey(JWT_SIGNING_KEY)
                    .parseClaimsJws(token.replace(BEARER,""))
                    .getBody()
                    .getAudience();
        }

        return null;
    }
}
