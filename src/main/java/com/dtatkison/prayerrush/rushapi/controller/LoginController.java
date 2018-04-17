package com.dtatkison.prayerrush.rushapi.controller;

import com.dtatkison.prayerrush.rushapi.model.User;
import com.dtatkison.prayerrush.rushapi.security.JwtGenerator;
import com.dtatkison.prayerrush.rushapi.service.UserService;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {
    private JwtGenerator jwtGenerator;
    private UserService userService;

    public LoginController(JwtGenerator jwtGenerator, UserService userService)
    {
        this.jwtGenerator = jwtGenerator;
        this.userService = userService;
    }

    @PostMapping
    public String generate(@RequestBody final User user)
    {
        Gson gson = new Gson();
        if(this.userService.loadByEmailAndPassword(user.getEmail(), user.getPassword()))
        {
            String json = gson.toJson(new JsonToken(jwtGenerator.generate(user)));
            return json;
        }

        return null;
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
