package com.hr.springboot.multi.tenant.app.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    protected LoginFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager) {
        super(defaultFilterProcessesUrl, authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        System.out.println("---- Attempting authentication in Login Filter ----");
        final AccountCredentials credentials = new ObjectMapper().readValue(request.getInputStream(), AccountCredentials.class);
        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword(), Collections.emptyList()));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException
    {
        System.out.println("---- Authentication is successful. So, creating the JWT token and setting in the header  ----");
        final Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        String tenant = null;
        for(GrantedAuthority authority : authorities)
        {
            tenant = authority.getAuthority();

            if(null!=tenant)
            {
                break;
            }
        }

        AuthenticationService.addToken(response, authResult.getName(), tenant);

    }
}
