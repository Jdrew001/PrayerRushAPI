package com.dtatkison.prayerrush.rushapi.controller;

import com.dtatkison.prayerrush.rushapi.model.User;
import com.dtatkison.prayerrush.rushapi.model.json.Friend;
import com.dtatkison.prayerrush.rushapi.model.json.Response;
import com.dtatkison.prayerrush.rushapi.security.JwtGenerator;
import com.dtatkison.prayerrush.rushapi.service.UserService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    public User checkUserProfile(@RequestBody final User user)
    {
        return null;
    }

    @PostMapping("/check")
    public ResponseEntity<String> checkUser(@RequestBody final User user)
    {
        Gson gson = new Gson();
        User u = this.userService.loadByEmail(user.getEmail());

        if(u.getUsername() == null || u.getFirstname() == null || u.getLastname() == null)
        {

            return new ResponseEntity<String>(gson.toJson(new Response("Information Null", false)), HttpStatus.OK);
        } else {
            return new ResponseEntity<String>(gson.toJson(u), HttpStatus.OK);
        }
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

    public class Success
    {
        private String successMessage;

        public void setSuccessMessage(String successMessage) {
            this.successMessage = successMessage;
        }
    }
    @PostMapping("/search/{email}")
    public List<User> getSearchedUser(@RequestBody User user, @PathVariable("email") String email)
    {
        return this.userService.searchedUsers(user, email);
    }

    @PostMapping("/friends")
    public List<User> getUserFriends(@RequestBody final User user)
    {
        return this.userService.getUserFollowers(user.getEmail());
    }

    @PostMapping("/friend/add/{email}")
    public List<User> addUserFriends(@RequestBody final User friend, @PathVariable("email") String email)
    {
        return this.userService.addUserFollowers(email, friend);
    }

    @PostMapping("/friend/requests")
    public List<User> getPendingFriendRequests(@RequestBody final User friend)
    {
        return this.userService.getPendingFriendRequests(friend.getEmail());
    }

    @PostMapping("/friend/requested")
    public List<User> getPendingFriendsRequested(@RequestBody final User friend)
    {
        return this.userService.getPendingFriendsRequested(friend.getEmail());
    }

    @PostMapping("/friend/requests/add/{email}")
    public List<User> addPendingFriendRequests(@RequestBody final User friend, @PathVariable("email") String email)
    {
        return this.userService.addPendingFriendRequest(email, friend);
    }

    @PostMapping("/friend/requests/remove/{email}")
    public List<User> removePendingFriendRequests(@RequestBody final User friend, @PathVariable("email") String email)
    {
        return this.userService.removePendingFriendRequests(email, friend);
    }

    @PostMapping("/friend/remove/{email}")
    public List<User> removeUserFriends(@RequestBody User friend, @PathVariable("email") String email)
    {
        return this.userService.removeUserFollowers(email, friend);
    }

    @PostMapping("/friend/acceptPending/{email}")
    public List<User> acceptUserFriends(@RequestBody User friend, @PathVariable("email") String email)
    {
        return this.userService.acceptFriendRequest(email, friend);
    }
}
