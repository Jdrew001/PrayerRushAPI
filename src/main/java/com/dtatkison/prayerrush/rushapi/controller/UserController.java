package com.dtatkison.prayerrush.rushapi.controller;

import com.dtatkison.prayerrush.rushapi.model.User;
import com.dtatkison.prayerrush.rushapi.security.JwtGenerator;
import com.dtatkison.prayerrush.rushapi.service.UserService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private JwtGenerator jwtGenerator;
    private UserService userService;

    @Autowired
    public UserController(JwtGenerator jwtGenerator, UserService userService)
    {
        this.jwtGenerator = jwtGenerator;
        this.userService = userService;
    }

    @PostMapping("/token")
    public String generateToken(@RequestBody final User user)
    {
        Gson gson = new Gson();
        return gson.toJson(new JsonToken(jwtGenerator.generate(this.userService.loadByEmail(user.getEmail()))));
    }

    @PostMapping("/update")
    public User updateUser(@RequestBody User user)
    {
        return this.userService.updateUserInformation(user);
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
