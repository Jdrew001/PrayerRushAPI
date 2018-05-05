package com.dtatkison.prayerrush.rushapi.controller;

import com.dtatkison.prayerrush.rushapi.model.DAO.RequestDAO;
import com.dtatkison.prayerrush.rushapi.model.Request;
import com.dtatkison.prayerrush.rushapi.model.User;
import com.dtatkison.prayerrush.rushapi.model.json.Response;
import com.dtatkison.prayerrush.rushapi.service.RequestService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/request")
@CrossOrigin
public class PrayerRequestController {

    private RequestService requestService;

    @Autowired
    public PrayerRequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping("/all")
    public List<RequestDAO> getAllRequests(@RequestBody final User user) {
        return this.requestService.getAllRequests(user);
    }

    @PostMapping("/add/{email}")
    public String addNewRequest(@RequestBody final Request request, @PathVariable String email)
    {
        this.requestService.addNewRequest(request, email);

        Gson gson = new Gson();
        String json = gson.toJson(new Response("Successfully added new Request", true));
        return json;
    }
}
