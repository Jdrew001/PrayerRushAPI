package com.dtatkison.prayerrush.rushapi.service;

import com.dtatkison.prayerrush.rushapi.model.DAO.FriendDAO;
import com.dtatkison.prayerrush.rushapi.model.JwtUserDetails;
import com.dtatkison.prayerrush.rushapi.model.User;
import com.dtatkison.prayerrush.rushapi.model.json.FriendRequestResponse;
import com.dtatkison.prayerrush.rushapi.repository.UserRepository;
import com.dtatkison.prayerrush.rushapi.security.ExceptionHandling.*;
import com.dtatkison.prayerrush.rushapi.security.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    private PasswordUtils passwordUtils;

    @Autowired
    SimpMessagingTemplate template;

    @Autowired
    public UserService(UserRepository userRepository, PasswordUtils passwordUtils)
    {
        this.userRepository = userRepository;
        this.passwordUtils = passwordUtils;
    }

    /**
     * This is called when the application is registering a user
     * @Param email - the new users email address
     * @Param password - this is the users password passed in
     * @Return User - this is the user object*/
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

    /**
     * Load the user by email and password
     * @Param Email - the email of the user
     * @Param Password - this is the password of the user
     * Returns User - user object that is returned*/
    public User loadByEmailAndPassword(String email, String password) throws EmailNotFoundException
    {
        Optional<User> user = userRepository.findByEmail(email);
        user.orElseThrow(() -> new EmailNotFoundException("The email address given was not found"));

        boolean passwordMatch = this.passwordUtils.verifyUserPassword(password, user.get().getPassword(), user.get().getSalt());

        if(!passwordMatch)
            throw new AuthenticationFailedError("Email and Password not authenticated");

        return user.map(JwtUserDetails::new).get();
    }

    /**
     * Load the user by his email
     * @Param Email - this is the email of the user that we want load
     * @Returns User - the user object from the database*/
    public User loadByEmail(String email)
    {
        Optional<User> user = userRepository.findByEmail(email);
        user.orElseThrow(() -> new EmailNotFoundException("The email address given was not found"));

        return user.map(JwtUserDetails::new).get();
    }

    public User getUser(String email)
    {
        Optional<User> user = userRepository.findByEmail(email);
        user.orElseThrow(() -> new EmailNotFoundException("The email address given was not found"));

        return user.get();
    }

    /**
     * Updates the user information
     * @Param User - this is the user that we want to update
     * @Returns User - this is the user we wanted to update*/
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

    /**
     * Search through a list of users through an email address
     * If the user is friends with the user then return an empty List
     * Also don't return the main user (logged in user)
     * @Param User user - the user that you want to search
     * @Param email - the user logged in
     * @Returns List of user - only return one user in a list*/
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

    /**
     * Returns a list of users. This is the users friends.
     * @Param email - this the logged in user's email
     * @Return List of users - this is the users followers
     * */
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
        user.orElseThrow(() -> new UserException("FriendDAO User not found"));
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

    /**
     * Remove a friend of a given user
     * @Param mainEmail - this is the email of logged in user
     * @Param follower - this is the user that the logged in user doesn't want to be friends with
     * @Return Users - Return all the friends of the main user*/
    public List<User> removeUserFollowers(String mainEmail, User follower)
    {
        Optional<User> user = this.userRepository.findByEmail(mainEmail);
        Optional<User> friendUser = this.userRepository.findByEmail(follower.getEmail());
        user.orElseThrow(() -> new UserException("Main user not found"));
        user.orElseThrow(() -> new UserException("FriendDAO User not found"));

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

    /**
     * add pending friend request to a given user
     * @Param mainEmail This is the email of the logged in user
     * @Param pendingFriend this is the user obj of the friend that you want to send a request to
     * @return return a list of users that you are pending friends with
     */
    public List<User> addPendingFriendRequest(String mainEmail, User pendingFriend)
    {
        Optional<User> user = this.userRepository.findByEmail(mainEmail);
        Optional<User> friendUser = this.userRepository.findByEmail(pendingFriend.getEmail());
        user.orElseThrow(() -> new UserException("Main user not found"));
        user.orElseThrow(() -> new UserException("FriendDAO User not found"));

        if(!user.get().getFollowers().contains(friendUser.get()) && !user.get().getFriendRequests().contains(friendUser.get()))
        {
            user.get().getFriendsRequested().add(friendUser.get());
            friendUser.get().getFriendRequests().add(user.get());
            FriendDAO friendDAO = new FriendDAO(friendUser.get().getFirstname(), friendUser.get().getLastname(), friendUser.get().getEmail());

            this.template.convertAndSend("/friend/"+friendUser.get().getEmail(),
                    new FriendRequestResponse(friendDAO,
                            "New Friend Request",friendUser.get().getFirstname() + " " + friendUser.get().getLastname() + " has requested to be your friend!"));

            this.userRepository.save(user.get());
            this.userRepository.save(friendUser.get());

            return getUserFollowers(user.get().getEmail());
        } else {
            throw new UserException("User is already friends with that person or they have a pending request");
        }
    }

    /**
     * Return the friend requests of the main logged in user
     * @Param email - this is the logged in user's email that will be used to retrieve the data
     * @Return Users - The list of users that is wanting to be friends with main user*/
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

    /**
     * Return the users that the main logged in user has requested to be friends with
     * @Param email - email of the main user that is logged in
     * @Returns List of users - these are the users the main user has requested to be friends with*/
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

    /**
     * Removes or (declines) a friend request for the logged in user (main user)
     * @Param mainUser - this is the logged in user's email address
     * @Param pendingFriend - this is the friend that is requesting friendship
     * @Returns users - return the list of pending requests to the mainUser*/
    public List<User> removePendingFriendRequests(String mainUser, User pendingFriend)
    {
        Optional<User> user = this.userRepository.findByEmail(mainUser);
        Optional<User> friendUser = this.userRepository.findByEmail(pendingFriend.getEmail());
        user.orElseThrow(() -> new UserException("Main user not found"));
        user.orElseThrow(() -> new UserException("FriendDAO User not found"));

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

    /**
     * Main user accepts a friend requests
     * @Param mainUser - the user that is logged in making the requests
     * @Param pendingFriend - the user that is requesting friendship
     * @Returns Returns null - return null*/
    public List<User> acceptFriendRequest(String mainUser, User pendingFriend)
    {
        //accept friend request
        //This method will delete the pending request and add to the
        Optional<User> user = this.userRepository.findByEmail(mainUser);
        Optional<User> friendUser = this.userRepository.findByEmail(pendingFriend.getEmail());
        user.orElseThrow(() -> new UserException("Main user not found"));
        user.orElseThrow(() -> new UserException("FriendDAO User not found"));

        if(user.get().getFriendRequests().contains(friendUser.get()))
        {
            //Remove
            user.get().getFriendRequests().remove(friendUser.get());
            friendUser.get().getFriendsRequested().remove(user.get());

            user.get().getFollowers().add(friendUser.get());
            friendUser.get().getFollowers().add(user.get());
            FriendDAO friendDAO = new FriendDAO(friendUser.get().getFirstname(), friendUser.get().getLastname(), friendUser.get().getEmail());

            this.template.convertAndSend("/friend/"+friendUser.get().getEmail(),
                    new FriendRequestResponse(friendDAO,
                            "New Friendship",user.get().getFirstname() + " " + user.get().getLastname() + " has accepted your request!"));

            this.userRepository.save(user.get());
            this.userRepository.save(friendUser.get());

        } else {
            throw new UserException("Failure accepting request");
        }

        return null; //TODO: return a list of user friends or a success message
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




























