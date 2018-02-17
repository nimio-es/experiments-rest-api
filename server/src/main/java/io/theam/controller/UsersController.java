package io.theam.controller;

import io.theam.model.User;
import io.theam.model.api.UserData;
import io.theam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UserService userService;

    /**
     * Utility method to transform a User model class to an API class returned
     * by the API and common to other modules.
     * @param modelUser
     * @return
     */
    private static UserData toUserData(User modelUser) {

        if(modelUser != null)
           return
                   new UserData(
                           modelUser.getUsername(),
                           new ArrayList<>(
                                   modelUser.getAuthorities().stream()
                                           .map(u -> u.getAuthority())
                                           .collect(Collectors.toList())));

        return null;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<UserData> getUserList() {
        final Collection<UserData> users =
                userService.findAll().stream()
                        .map(UsersController::toUserData)
                        .collect(Collectors.toList());

        return users;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public UserData addUser(
            @RequestParam(name = "username") String username,
            @RequestParam(name = "password") String password,
            @RequestParam(name = "asAdmin", required = false, defaultValue = "false") boolean asAdmin) {

        return toUserData(userService.addUser(username, password, asAdmin));
    }
}
