package com.dtatkison.prayerrush.rushapi.security;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AuthenticationFailedError extends RuntimeException {
    public AuthenticationFailedError(String exception)
    {
        super(exception);
    }
}