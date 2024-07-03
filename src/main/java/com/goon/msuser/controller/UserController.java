package com.goon.msuser.controller;

import com.goon.msuser.entities.User;
import com.goon.msuser.entities.dto.UserDTO;
import com.goon.msuser.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RefreshScope
@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll() {
        List<User> objectList = userRepository.findAll();
        List<UserDTO> dtoObject = objectList.stream().map( UserDTO::new).collect( Collectors.toList());
        return ResponseEntity.ok().body( dtoObject );
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
            User userObject = userRepository.findById(id).orElseThrow();
            UserDTO userDTO = new UserDTO(userObject);
            return ResponseEntity.ok().body(userDTO);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<UserDTO> findByEmail(@RequestParam String email) {
        User userObject = userRepository.findByEmail(email);
        UserDTO userDTO = new UserDTO(userObject);
        if(userDTO == null){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body( userDTO );
    }

    @PostMapping(value = "/new")
    public ResponseEntity<UserDTO> createUser(@RequestBody User userObject){
        String enc = bCryptPasswordEncoder.encode(userObject.getPassword());
        userObject.setPassword(enc);
        userObject = userRepository.save(userObject);
        UserDTO dto = new UserDTO(userObject);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);

    }
}
