package com.dtatkison.prayerrush.rushapi.controller;

import com.dtatkison.prayerrush.rushapi.model.User;
import com.dtatkison.prayerrush.rushapi.security.JwtGenerator;
import com.dtatkison.prayerrush.rushapi.service.UserService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@CrossOrigin //TODO research pitfalls of putting this on here
public class LoginController {
    private JwtGenerator jwtGenerator;
    private UserService userService;

    @Autowired
    public LoginController(JwtGenerator jwtGenerator, UserService userService)
    {
        this.jwtGenerator = jwtGenerator;
        this.userService = userService;
    }

    @PostMapping("/login")
    public String generate(@RequestBody final User user)
    {
        Gson gson = new Gson();
        User u = this.userService.loadByEmailAndPassword(user.getEmail(), user.getPassword());
        String json = gson.toJson(new JsonToken(jwtGenerator.generate(u)));
        return json;
    }

    @GetMapping("/api")
    public ResponseEntity<?> checkToken()
    {
        Gson gson = new Gson();
        String json = gson.toJson(new Success("Success"));
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    public class Success
    {
        private String success;
        public Success(String success)
        {
            this.success = success;
        }
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
