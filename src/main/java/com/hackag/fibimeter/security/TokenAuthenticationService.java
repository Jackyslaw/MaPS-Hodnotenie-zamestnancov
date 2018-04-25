package com.hackag.fibimeter.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

class TokenAuthenticationService {

    static final long EXPIRATIONTIME = 86_400_000; // 1 days
    static final String SECRET = "ThisIsASecret";
    static final String AUTH_HEADER_STRING = "Authorization";
    static final String AUTHORITIES_KEY = "authorities";

    static void addAuthentication(HttpServletResponse res, String username,
                                  Collection<? extends GrantedAuthority> authorities) throws IOException {
        String JWT = Jwts.builder()
            .setSubject(username)
            .claim(AUTHORITIES_KEY, serializeAuthorities(authorities))
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
            .signWith(SignatureAlgorithm.HS512, SECRET)
            .compact();
        res.addHeader(AUTH_HEADER_STRING, JWT);
        res.getWriter().write("{ \"admin\": " + String.valueOf(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) + " }");
    }

    static Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(AUTH_HEADER_STRING);
        if (token != null) {
            // parse the token.
            Claims claims = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
            String user = claims.getSubject();
            Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            return user != null ?
                new UsernamePasswordAuthenticationToken(user, null, authorities) :
                null;
        }
        return null;
    }

    private static String serializeAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));
    }
}
