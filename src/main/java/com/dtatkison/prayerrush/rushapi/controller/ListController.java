package com.dtatkison.prayerrush.rushapi.controller;

import com.dtatkison.prayerrush.rushapi.model.User;
import com.dtatkison.prayerrush.rushapi.service.ListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/list")
public class ListController {

    @Autowired
    private ListService listService;

    public ListController(ListService listService) {
        this.listService = listService;
    }

    //get all user lists
    @PostMapping("/getUserList")
    public List<com.dtatkison.prayerrush.rushapi.model.List> getAllUserLists(@RequestBody final User user) {
        return this.listService.getAllUserLists(user.getEmail());
    }

    //update list item -- pass in a list obj

    //add new list item -- pass in a list obj
    @PostMapping("/addUserList/{email}")
    public List<com.dtatkison.prayerrush.rushapi.model.List> addUserList(@RequestBody final com.dtatkison.prayerrush.rushapi.model.List list, @PathVariable("email") String email) {
        return this.listService.addNewList(list, email);
    }

    //update user list
    @PostMapping("/updateListItem/{email}")
    public List<com.dtatkison.prayerrush.rushapi.model.List> updateUserList(@RequestBody final com.dtatkison.prayerrush.rushapi.model.List list, @PathVariable("email") String email) {
        return this.listService.updateUserList(list, email);
    }

    @PostMapping("/deleteUserList/{email}")
    public List<com.dtatkison.prayerrush.rushapi.model.List> deleteUserList(@RequestBody final com.dtatkison.prayerrush.rushapi.model.List list, @PathVariable("email") String email) {
        return this.listService.deleteList(list, email);
    }
}
