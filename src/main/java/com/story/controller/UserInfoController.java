package com.story.controller;

import com.story.exception.ResourceNotFoundException;
import com.story.exception.ResourceConflictException;
import com.story.model.UserInfo;
import com.story.repository.UserInfoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
//@RequestMapping("/api/v1")
public class UserInfoController {

    @Autowired
    private UserInfoRepository userInfoRepository;


    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public List<UserInfo> getAllUsers() {
        return userInfoRepository.findAll();
    }


    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public ResponseEntity<UserInfo> getUserById(@PathVariable(value = "id") Integer userId) throws ResourceNotFoundException {
        UserInfo userInfo = userInfoRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
        return ResponseEntity.ok().body(userInfo);
    }


    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public ResponseEntity<UserInfo> createUser(@Valid @RequestBody UserInfo user) throws ResourceConflictException {

        String username = user.getUsername();
        if(userInfoRepository.existsByUsername(username)) {
            throw new ResourceConflictException("Username already exist");
        }

        String password = user.getPassword();
        String encodedPassword = new BCryptPasswordEncoder().encode(password);

        final UserInfo newUser = userInfoRepository.save(new UserInfo(username, encodedPassword));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newUser.getId()).toUri();
        return ResponseEntity.created(location).body(newUser);
    }


    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
    public ResponseEntity<UserInfo> updateUser(@PathVariable(value = "id") Integer userId,
                                               @Valid @RequestBody UserInfo userDetails) throws Exception {
        UserInfo user = userInfoRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
        String username = userDetails.getUsername();
        if(userInfoRepository.existsByUsername(username)) {
            throw new ResourceConflictException("Username " + "'" + username + "'" + " already taken.");
        }

        user.setUsername(username);
        final UserInfo updatedUser = userInfoRepository.save(user);
        return ResponseEntity.ok(updatedUser);
    }


    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Map> deleteUser(@PathVariable(value = "id") Integer userId) throws ResourceNotFoundException {

        UserInfo user = userInfoRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
        userInfoRepository.delete(user);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

}