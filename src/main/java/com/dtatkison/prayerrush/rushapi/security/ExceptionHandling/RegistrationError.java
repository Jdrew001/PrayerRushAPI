package com.dtatkison.prayerrush.rushapi.security.ExceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RegistrationError extends AuthenticationFailedError {
    public RegistrationError(String exception) {
        super(exception);
    }
}
