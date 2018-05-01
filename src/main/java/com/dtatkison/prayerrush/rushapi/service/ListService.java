package com.dtatkison.prayerrush.rushapi.service;

import com.dtatkison.prayerrush.rushapi.model.List;
import com.dtatkison.prayerrush.rushapi.model.User;
import com.dtatkison.prayerrush.rushapi.repository.ListRepository;
import com.dtatkison.prayerrush.rushapi.repository.UserRepository;
import com.dtatkison.prayerrush.rushapi.security.ExceptionHandling.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ListService {

    //get all user lists

    //update list item -- pass in a list obj

    //add new list item -- pass in a list obj

    //delete list item -- pass in a list obj

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ListRepository listRepository;

    public ListService(UserRepository userRepository, ListRepository listRepository) {
        this.userRepository = userRepository;
        this.listRepository = listRepository;
    }

    public java.util.List<List> getAllUserLists(String email)
    {
        Optional<User> user = this.userRepository.findByEmail(email);
        user.orElseThrow(() -> new UserException("User not found"));

        return user.get().getLists();
    }

    public java.util.List<List> addNewList(List list, String email)
    {
        Optional<User> user = this.userRepository.findByEmail(email);
        user.orElseThrow(() -> new UserException("User not found"));
        list.setDate(new Date());

        List newList = new List(list);

        user.get().addToList(newList);
        this.userRepository.save(user.get());
        return user.get().getLists();
    }

    public java.util.List<List> updateUserList(List list, String email)
    {


        List listObj = this.listRepository.getOne(list.getListId());

        //find a list objssssssaaaaaaas
        listObj.setName(list.getName());
        listObj.setDescription(list.getDescription());
        int result = this.listRepository.updateList(listObj.getName(), listObj.getDescription(), listObj.getDate(), listObj.getListId());
        System.out.println("RESULT!!!: " + result);

        Optional<User> user = this.userRepository.findByEmail(email);
        user.orElseThrow(() -> new UserException("User not found"));

        return user.get().getLists();
    }

    public java.util.List<List> deleteList(List list, String email)
    {
        Optional<User> user = this.userRepository.findByEmail(email);
        user.orElseThrow(() -> new UserException("User not found"));

        Optional<List> listObj = this.listRepository.findByListId(list.getListId());
        listObj.orElseThrow(() -> new UserException("List not found"));

        this.listRepository.delete(listObj.get());

        return user.get().getLists();
    }
}
