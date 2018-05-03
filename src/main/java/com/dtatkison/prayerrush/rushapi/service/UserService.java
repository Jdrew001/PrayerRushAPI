package com.dtatkison.prayerrush.rushapi.service;

import com.dtatkison.prayerrush.rushapi.controller.LoginController;
import com.dtatkison.prayerrush.rushapi.model.JwtUserDetails;
import com.dtatkison.prayerrush.rushapi.model.User;
import com.dtatkison.prayerrush.rushapi.repository.UserRepository;
import com.dtatkison.prayerrush.rushapi.security.ExceptionHandling.*;
import com.dtatkison.prayerrush.rushapi.security.PasswordUtils;
import com.google.gson.Gson;
import org.hibernate.dialect.Ingres9Dialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
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

    public User loadByEmailAndPassword(String email, String password) throws EmailNotFoundException
    {
        Optional<User> user = userRepository.findByEmail(email);
        user.orElseThrow(() -> new EmailNotFoundException("The email address given was not found"));

        boolean passwordMatch = this.passwordUtils.verifyUserPassword(password, user.get().getPassword(), user.get().getSalt());

        if(!passwordMatch)
            throw new AuthenticationFailedError("Email and Password not authenticated");

        return user.map(JwtUserDetails::new).get();
    }

    public User loadByEmail(String email)
    {
        Optional<User> user = userRepository.findByEmail(email);
        user.orElseThrow(() -> new EmailNotFoundException("The email address given was not found"));

        return user.map(JwtUserDetails::new).get();
    }

    public User updateUserInformation(User user)
    {
        Optional<User> u = this.userRepository.findByEmail(user.getEmail());
        u.orElseThrow(() -> new UserException("User not found"));

        u.get().setFirstname(user.getFirstname());
        u.get().setLastname(user.getLastname());
        u.get().setUsername(user.getUsername());
        u.get().setEmail(user.getEmail());

        User newUser = this.userRepository.save(u.get());
        User resultU = new User(newUser.getId(), newUser.getFirstname(), newUser.getLastname(), newUser.getUsername(), newUser.getEmail());

        return resultU;
    }

    public List<User> searchedUsers(User user, String email)
    {
        List<User> users = new ArrayList<>();
        List<User> requestedUsers = getPendingFriendsRequested(email);
        List<User> friends = getUserFollowers(email);
        //user has passed in an email, making the search easy
        if(user.getEmail().equals(email))
            return users;

        for (User tempUser: requestedUsers)
            if(user.getEmail().equals(tempUser.getEmail()))
                return users;

        for(User tempUser: friends) 
            if(user.getEmail().equals(tempUser.getEmail()))
                return users;

            Optional<User> u = this.userRepository.findByEmail(user.getEmail());

            u.get().setPassword("");
            u.get().setSalt("");
            u.get().removeAllLists();
            users.add(u.get());

            return users;
    }

    //get user followers
    public List<User> getUserFollowers(String email)
    {
            //with the user id, get the
        Optional<User> user = this.userRepository.findByEmail(email);
        user.orElseThrow(() -> new UserException("User not found"));
        for (User tempUser: user.get().getFollowers()) {
            tempUser.removeAllLists();
            tempUser.setPassword("");
            tempUser.setSalt("");
        }

        return user.get().getFollowers();
    }

    /**
    * Returns a List of users once you add a new friend to a specific user.
    * @Param mainEmail This is the email of the logged in user.
    * @Param follower This is a user Object of the user that will become friends with logged in user
    * @return List of the users friends*/
    public List<User> addUserFollowers(String mainEmail, User follower)
    {
        Optional<User> user = this.userRepository.findByEmail(mainEmail);
        Optional<User> friendUser = this.userRepository.findByEmail(follower.getEmail());

        user.orElseThrow(() -> new UserException("Main user not found"));
        user.orElseThrow(() -> new UserException("Friend User not found"));
        User u = user.get();

        if(!u.getFollowers().contains(friendUser.get()))
        {
            u.getFollowers().add(friendUser.get());
            friendUser.get().getFollowers().add(user.get());
            this.userRepository.save(u);
            this.userRepository.save(friendUser.get());

            return u.getFollowers();
        } else {
            throw new UserException("User is already friends with that person");
        }
    }

    public List<User> removeUserFollowers(String mainEmail, User follower)
    {
        Optional<User> user = this.userRepository.findByEmail(mainEmail);
        Optional<User> friendUser = this.userRepository.findByEmail(follower.getEmail());
        user.orElseThrow(() -> new UserException("Main user not found"));
        user.orElseThrow(() -> new UserException("Friend User not found"));

        if(user.get().getFollowers().contains(friendUser.get()))
        {
            user.get().getFollowers().remove(friendUser.get());
            friendUser.get().getFollowers().remove(user.get());
            this.userRepository.save(user.get());
            this.userRepository.save(friendUser.get());

            return user.get().getFollowers();
        } else {
            throw new UserException("User isn't friends with that person");
        }
    }

    //user and his pending friend requests
    /**
     * add pending friend requests to a given user
     * @Param mainEmail This is the email of the logged in user
     * @Param pendingFriend this is the user obj of the friend that you want to send a request to
     * @return return a list of users that you are pending friends with
     */
    public List<User> addPendingFriendRequest(String mainEmail, User pendingFriend)
    {
        Optional<User> user = this.userRepository.findByEmail(mainEmail);
        Optional<User> friendUser = this.userRepository.findByEmail(pendingFriend.getEmail());
        user.orElseThrow(() -> new UserException("Main user not found"));
        user.orElseThrow(() -> new UserException("Friend User not found"));

        if(!user.get().getFollowers().contains(friendUser.get()) && !user.get().getFriendRequests().contains(friendUser.get()))
        {
            user.get().getFriendsRequested().add(friendUser.get());
            friendUser.get().getFriendRequests().add(user.get());
            this.userRepository.save(user.get());
            this.userRepository.save(friendUser.get());

            return getUserFollowers(user.get().getEmail());
        } else {
            throw new UserException("User is already friends with that person or they have a pending request");
        }
    }

    public List<User> getPendingFriendRequests(String email)
    {
        Optional<User> user = this.userRepository.findByEmail(email);
        user.orElseThrow(() -> new UserException("User not found"));
        for (User tempUser: user.get().getFriendRequests()) {
            tempUser.removeAllLists();
            tempUser.setPassword("");
            tempUser.setSalt("");
        }

        return user.get().getFriendRequests();
    }

    public List<User> getPendingFriendsRequested(String email)
    {
        Optional<User> user = this.userRepository.findByEmail(email);
        user.orElseThrow(() -> new UserException("User not found"));
        for (User tempUser: user.get().getFriendsRequested()) {
            tempUser.removeAllLists();
            tempUser.setPassword("");
            tempUser.setSalt("");
        }

        return user.get().getFriendsRequested();
    }

    public List<User> removePendingFriendRequests(String mainUser, User pendingFriend)
    {
        Optional<User> user = this.userRepository.findByEmail(mainUser);
        Optional<User> friendUser = this.userRepository.findByEmail(pendingFriend.getEmail());
        user.orElseThrow(() -> new UserException("Main user not found"));
        user.orElseThrow(() -> new UserException("Friend User not found"));

        if(user.get().getFriendRequests().contains(friendUser.get()))
        {
            user.get().getFriendRequests().remove(friendUser.get());
            friendUser.get().getFriendsRequested().remove(user.get());
            this.userRepository.save(user.get());
            this.userRepository.save(friendUser.get());

            return getPendingFriendRequests(user.get().getEmail());
        } else {
            throw new UserException("User isn't requesting to be friends");
        }
    }

    public List<User> acceptFriendRequest(String mainUser, User pendingFriend)
    {
        //accept friend request
        //This method will delete the pending request and add to the
        Optional<User> user = this.userRepository.findByEmail(mainUser);
        Optional<User> friendUser = this.userRepository.findByEmail(pendingFriend.getEmail());
        user.orElseThrow(() -> new UserException("Main user not found"));
        user.orElseThrow(() -> new UserException("Friend User not found"));

        if(user.get().getFriendRequests().contains(friendUser.get()))
        {
            //Remove
            user.get().getFriendRequests().remove(friendUser.get());
            friendUser.get().getFriendsRequested().remove(user.get());

            user.get().getFollowers().add(friendUser.get());
            friendUser.get().getFollowers().add(user.get());

            this.userRepository.save(user.get());
            this.userRepository.save(friendUser.get());

        } else {
            throw new UserException("Failure accepting request");
        }

        return null;
    }

    public class Success
    {
        private String success;
        public Success(String success)
        {
            this.success = success;
        }
    }

}




























