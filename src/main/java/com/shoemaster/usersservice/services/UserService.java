package com.shoemaster.usersservice.services;

import com.shoemaster.usersservice.enums.Roles;
import com.shoemaster.usersservice.repositories.UserRepository;
import com.shoemaster.usersservice.models.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public User getUserById(Long id) {
        Optional<User> optional;
        if ((optional = userRepository.findById(id)).isEmpty()) {
            return null;
        } else {
            return optional.get();
        }
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public User createNewUser(User user) {
        user.setRoles(List.of(Roles.ROLE_USER));
        return userRepository.save(user);
    }

    public User registerUser(User user) {
        // Hash the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save user to the database
        return userRepository.save(user);
    }

//    public void registerUser(User user) {
//        if (userRepository.existsByUsername(user.getUsername())) {
//            throw new RuntimeException("Email already in use");
//        }

    public User updateUser(User updatedUser) {
        User user = userRepository.findByUsername(updatedUser.getUsername());
        BeanUtils.copyProperties(updatedUser, user);
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public List<User> getAllUsers       () {
        return userRepository.findAll();
    }

    public void promote(User user) {

    }
}
