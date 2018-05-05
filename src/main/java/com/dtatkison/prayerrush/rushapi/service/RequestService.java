package com.dtatkison.prayerrush.rushapi.service;

import com.dtatkison.prayerrush.rushapi.model.DAO.RequestDAO;
import com.dtatkison.prayerrush.rushapi.model.Request;
import com.dtatkison.prayerrush.rushapi.model.User;
import com.dtatkison.prayerrush.rushapi.model.json.PrayerRequestResponse;
import com.dtatkison.prayerrush.rushapi.repository.RequestRepository;
import com.dtatkison.prayerrush.rushapi.repository.UserRepository;
import com.dtatkison.prayerrush.rushapi.security.ExceptionHandling.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.RequestDataValueProcessor;

import java.util.*;

@Service
public class RequestService {

    private RequestRepository requestRepository;
    private UserService userService;
    private UserRepository userRepository;

    @Autowired
    SimpMessagingTemplate template;

    @Autowired
    public RequestService(RequestRepository requestRepository, UserService userService, UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    /**
     * Get the all the prayer requests for a given user and the friends of his
     * @Param User obj - this will be where we get the email of the logged in user
     * @Returns List of Requests - this will return the data access object request*/
    public List<RequestDAO> getAllRequests(User user) {

        User mainUser = this.userService.getUser(user.getEmail());
        List<User> users = mainUser.getFollowers();
        List<RequestDAO> requests = new ArrayList<>();

        for (Request r : mainUser.getRequests()) {
            requests.add(new RequestDAO(r.getRequestId(), r.getDescription(), r.getDate(), mainUser.getFirstname(), mainUser.getLastname(), mainUser.getEmail()));
        }

        for (User u: users) {
            System.out.println(u.getUsername());
            for(Request r: u.getRequests()) {
                requests.add(new RequestDAO(r.getRequestId(), r.getDescription(), r.getDate(), u.getFirstname(), u.getLastname(), u.getEmail()));
            }
        }


        requests.sort(Comparator.comparing(RequestDAO::getDate).reversed());

        return requests;
    }

    /**
     * This is called when the logged in user wants to add a new request amongst there friends
     * This method will send out updates to every one of the user's friends when it is successfully added using websocket
     * @Param Request - this is the request object that is passed in
     * @Param email - this is the email of the user adding the request
     * */
    public void addNewRequest(Request request, String email)
    {
        Optional<User> mainUser = this.userRepository.findByEmail(email);
        mainUser.orElseThrow(() -> new UserException("User not found"));

        List<User> followers = mainUser.get().getFollowers();
        request.setDate(new Date());

        Request newRequest = new Request(request);
        mainUser.get().getRequests().add(newRequest);

        RequestDAO requestDAO = new RequestDAO(newRequest.getDescription(), newRequest.getDate(), mainUser.get().getFirstname(), mainUser.get().getLastname(), mainUser.get().getEmail());

        this.userRepository.save(mainUser.get());

        if(!followers.isEmpty()) {
            for(int i = 0; i < followers.size(); i++) {
                this.template.convertAndSend("/request/"+followers.get(i).getEmail(),
                        new PrayerRequestResponse(requestDAO, "New Prayer Requests", mainUser.get().getFirstname() + " has added a new prayer request! Go check it out!"));
            }
        }
    }




































}
