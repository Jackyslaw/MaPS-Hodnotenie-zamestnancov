package com.hackag.fibimeter.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthenticationFilter extends GenericFilterBean {
    private static Logger log = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain)
        throws IOException, ServletException {
        Authentication authentication = null;
        try {
            authentication = TokenAuthenticationService
                .getAuthentication((HttpServletRequest) request);
        } catch (ExpiredJwtException e) {
            log.debug(e.toString());
            ((HttpServletResponse) response).setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return;
        } catch (JwtException e) {
            log.warn(e.toString());
            ((HttpServletResponse) response).setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return;
        }
        SecurityContextHolder.getContext()
            .setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
