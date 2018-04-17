package com.dtatkison.prayerrush.rushapi.controller;

import com.dtatkison.prayerrush.rushapi.model.User;
import com.dtatkison.prayerrush.rushapi.security.JwtGenerator;
import com.dtatkison.prayerrush.rushapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/register")
public class RegistrationController {

    private UserService userService;
    private JwtGenerator jwtGenerator;

    @Autowired
    public RegistrationController(UserService userService, JwtGenerator jwtGenerator)
    {
        this.userService = userService;
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping
    public String register(@RequestBody final User jwtUser)
    {
        this.userService.registerUser(jwtUser.getEmail(), jwtUser.getPassword());

        return jwtGenerator.generate(jwtUser);
    }
}
