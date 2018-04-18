package com.dtatkison.prayerrush.rushapi.service;

import com.dtatkison.prayerrush.rushapi.model.JwtUserDetails;
import com.dtatkison.prayerrush.rushapi.model.User;
import com.dtatkison.prayerrush.rushapi.repository.UserRepository;
import com.dtatkison.prayerrush.rushapi.security.ExceptionHandling.*;
import com.dtatkison.prayerrush.rushapi.security.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    private PasswordUtils passwordUtils;

    @Autowired
    public UserService(UserRepository userRepository, PasswordUtils passwordUtils)
    {
        this.userRepository = userRepository;
        this.passwordUtils = passwordUtils;
    }

    public User registerUser(String email, String password)
    {
        //check to see if email already exists
        Optional<User> u = userRepository.findByEmail(email);
        if(u.isPresent())
        {
            throw new RegistrationError("Email already exists. Please enter a new email");
        }

        String salt = this.passwordUtils.getSalt(30);
        String securePassword = this.passwordUtils.generateSecurePassword(password, salt);
        User user = new User(email, securePassword, salt);
        userRepository.save(user);

        Optional<User> newUser = userRepository.findByEmailAndPassword(email, securePassword);
        newUser.orElseThrow(() -> new RegistrationError("Email not found!"));

        return newUser.map(JwtUserDetails::new).get();
    }

    public boolean loadByEmailAndPassword(String email, String password) throws EmailNotFoundException
    {
        Optional<User> user = userRepository.findByEmail(email);
        user.orElseThrow(() -> new EmailNotFoundException("The email address given was not found"));

        boolean passwordMatch = this.passwordUtils.verifyUserPassword(password, user.get().getPassword(), user.get().getSalt());

        if(!passwordMatch)
            throw new AuthenticationFailedError("Email and Password not authenticated");

        return passwordMatch;
    }

    public User loadByEmail(String email)
    {
        Optional<User> user = userRepository.findByEmail(email);
        user.orElseThrow(() -> new EmailNotFoundException("The email address given was not found"));

        return user.map(JwtUserDetails::new).get();
    }

    public User updateUserInformation(User user)
    {
        Optional<User> u = this.userRepository.findById(user.getId());
        u.orElseThrow(() -> new UserException("User not found"));

        u.get().setFirstname(user.getFirstname());
        u.get().setLastname(user.getLastname());
        u.get().setUsername(user.getUsername());
        u.get().setEmail(user.getEmail());

        User newUser = this.userRepository.save(u.get());
        User resultU = new User(newUser.getId(), newUser.getFirstname(), newUser.getLastname(), newUser.getUsername(), newUser.getEmail());

        return resultU;
    }
}




























