package com.shoemaster.usersservice.controllers;

import com.shoemaster.usersservice.models.User;
import com.shoemaster.usersservice.repositories.UserRepository;
import com.shoemaster.usersservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/promote")
    public ResponseEntity<?> promote(@RequestBody User user){
        userService.promote(user);
        return ResponseEntity.ok().body(null);
    }

//    @PostMapping("/register")
//    public User registerUser(@RequestBody User user) {
//        return userService.registerUser(user);
//    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            // checking if username is in use
            if (userService.existsByUsername(user.getUsername())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already in use.");
            }

            // creating and saving new user
            User newUser = userService.createNewUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed: " + e.getMessage());
        }
    }

    @GetMapping()
    public List<User> getAll(){
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    @PostMapping
//    public ResponseEntity<?> createNewUser(@RequestBody User user) {
//        try {
//            User newUser = userService.createNewUser(user);
//            return ResponseEntity.created(URI.create("/user/" + newUser.getId())).body(newUser);
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body(e.getMessage());
//        }
//    }

    @PatchMapping
    public ResponseEntity<?> updateUser(@RequestBody User updatedUser) {
        try {
            return ResponseEntity.ok(userService.updateUser(updatedUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("user with " + id + " deleted");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/user-list")
    public String displayUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "user-list";
    }

}
