package com.dtatkison.prayerrush.rushapi.security.ExceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmailNotFoundException extends AuthenticationFailedError {
    public EmailNotFoundException(String exception) {
        super(exception);
    }
}
