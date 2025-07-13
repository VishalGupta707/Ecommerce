package com.projects.Ecommerce.Service;

import com.projects.Ecommerce.Model.User;
import com.projects.Ecommerce.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {
        try{
            User save = userRepository.save(user);
            System.out.println("User Added");
            return save;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/login")
    public User loginUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if(user!=null && user.getPassword().equals(password)){
            return user;
        }
        return null;

    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
