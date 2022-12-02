package com.example.ppmtools.services;

import com.example.ppmtools.domain.User;
import com.example.ppmtools.exception.UsernameAlreadyExistsException;
import com.example.ppmtools.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser(User newUser){

        try{
            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
            //Username has to be unique (custom exception)
            newUser.setUsername(newUser.getUsername());
            //Make sure that password & confirmPassword match
            //We don't persist or show confirmPassword
            newUser.setConfirmPassword("");
            return userRepository.save(newUser);
        }catch (Exception e){
            throw new UsernameAlreadyExistsException("Username `"+newUser.getUsername()+"' already exists");
        }

    }


}
