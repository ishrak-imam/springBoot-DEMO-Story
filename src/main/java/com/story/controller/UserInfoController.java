package com.story.controller;



import com.story.exception.ValidationException;
import com.story.model.UserInfo;
import com.story.repository.UserInfoRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@RestController
public class UserInfoController {

    final private UserInfoRepository userInfoRepository;

    public UserInfoController(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }


    @PostMapping("/user")
    public Boolean create(@RequestBody Map<String, String> body) throws NoSuchAlgorithmException {
        String username = body.get("username");
        if (userInfoRepository.existsByUsername(username)){
            throw new ValidationException("Username already existed");
        }

        String password = body.get("password");
        String encodedPassword = new BCryptPasswordEncoder().encode(password);
        userInfoRepository.save(new UserInfo(username, encodedPassword));
        return true;
    }

}