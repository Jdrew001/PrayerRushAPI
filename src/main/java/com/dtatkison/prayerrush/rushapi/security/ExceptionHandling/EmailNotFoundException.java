package com.dtatkison.prayerrush.rushapi.security.ExceptionHandling;

public class EmailNotFoundException extends AuthenticationFailedError {
    public EmailNotFoundException(String exception) {
        super(exception);
    }
}
