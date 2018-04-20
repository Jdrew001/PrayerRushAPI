package com.dtatkison.prayerrush.rushapi.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class JwtAuthenticationTokenFilter extends AbstractAuthenticationProcessingFilter {
    public JwtAuthenticationTokenFilter() {
        super("/api/**");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {

        String origin = httpServletRequest.getHeader("Origin");
        final List<String> allowedOrigins = Arrays.asList("http://localhost:8100", "http://192.168.56.1:8100", "http://192.168.200.2:8100", "http://192.168.1.9:8100");
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.setHeader("Vary", "Origin");

        // Access-Control-Max-Age
        httpServletResponse.setHeader("Access-Control-Max-Age", "3600");

        // Access-Control-Allow-Credentials
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");

        // Access-Control-Allow-Methods
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");

        // Access-Control-Allow-Headers
        httpServletResponse.setHeader("Access-Control-Allow-Headers",
                "Origin, Authorization, myheader, X-Requested-By, X-Requested-With, Content-Type, Accept, " + "X-CSRF-TOKEN");


        if(httpServletRequest.getMethod().equals("OPTIONS")) {
            httpServletResponse.flushBuffer();
        } else {
            String header = httpServletRequest.getHeader("Authorization");
            if (header == null || !header.startsWith("Token ")) {
                throw new RuntimeException("JWT Token is missing");
            }

            String authenticationToken = header.substring(6);

            JwtAuthenticationToken token = new JwtAuthenticationToken(authenticationToken);
            return getAuthenticationManager().authenticate(token);
        }

        return null;
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }
}
