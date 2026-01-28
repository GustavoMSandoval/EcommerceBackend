package com.api.EcommerceBackend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.EcommerceBackend.user.dto.UserRequest;
import com.api.EcommerceBackend.user.dto.UserResponse;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200"})
public class UserController {

    @Autowired
    private UserService service;

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@RequestBody UserRequest request){

        return service.uptade(request);
    }
}
