package com.dtatkison.prayerrush.rushapi.security.ExceptionHandling;

public class UserException extends RuntimeException {
    public UserException(String exception)
    {
        super(exception);
    }
}
