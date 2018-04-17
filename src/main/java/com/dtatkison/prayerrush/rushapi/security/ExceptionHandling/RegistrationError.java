package com.dtatkison.prayerrush.rushapi.security.ExceptionHandling;

public class RegistrationError extends AuthenticationFailedError {
    public RegistrationError(String exception) {
        super(exception);
    }
}
