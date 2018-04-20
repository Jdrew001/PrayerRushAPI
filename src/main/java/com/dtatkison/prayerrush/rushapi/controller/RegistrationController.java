package com.dtatkison.prayerrush.rushapi.controller;

import com.dtatkison.prayerrush.rushapi.model.User;
import com.dtatkison.prayerrush.rushapi.security.JwtGenerator;
import com.dtatkison.prayerrush.rushapi.service.UserService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/register")
@CrossOrigin
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
        Gson gson = new Gson();
        User user = this.userService.registerUser(jwtUser.getEmail(), jwtUser.getPassword());
        return gson.toJson(new JsonToken(jwtGenerator.generate(user)));
    }

    public class JsonToken
    {
        private String token;
        public JsonToken(String token)
        {
            this.token = token;
        }
    }
}
