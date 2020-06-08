package net.javahotel.controller;

import net.javahotel.exception.ResourceNotFoundException;
import net.javahotel.model.Users;
import net.javahotel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    //get users
    @GetMapping("/users")
    public List<Users> getAllUser(){
        return this.userRepository.findAll();
    }

    //get users by id
    @GetMapping("/users/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable(value = "id") Long userId)
            throws ResourceNotFoundException {
        Users users = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));
        return ResponseEntity.ok().body(users);
    }

    //create users
    @PostMapping("/users/post")
    public Users createUser(@Valid @RequestBody Users users) {
        return userRepository.save(users);
    }
    //update users
    @PutMapping("/users/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable(value = "id") Long userId,
                                            @Valid @RequestBody Users usersDetails) throws ResourceNotFoundException {
        Users users = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Users not found for this id :: " + userId));

        users.setMiddleName(usersDetails.getMiddleName());
        users.setLastName(usersDetails.getLastName());
        users.setFirstName(usersDetails.getFirstName());
        final Users updatedUsers = userRepository.save(users);
        return ResponseEntity.ok(updatedUsers);
    }


    //delete users
    @DeleteMapping("/users/{id}")
    public Map<String, Boolean> deleteUser(@PathVariable(value = "id") Long userId)
            throws ResourceNotFoundException {
        Users users = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Users not found for this id :: " + userId));

        userRepository.delete(users);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
