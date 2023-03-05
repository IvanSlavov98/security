package com.example.security.controllers;

import com.example.security.dtos.AuthResponseDTO;
import com.example.security.dtos.LoginDTO;
import com.example.security.dtos.RegisterDTO;
import com.example.security.entities.AppUser;
import com.example.security.entities.Role;
import com.example.security.repositories.AppUserRepository;
import com.example.security.repositories.RoleRepository;
import com.example.security.security.JWTGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final JWTGenerator jwtGenerator;


    @Autowired
    public AuthController(AuthenticationManager authenticationManager, AppUserRepository appUserRepository,
                          RoleRepository roleRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper, JWTGenerator jwtGenerator) {
        this.authenticationManager = authenticationManager;
        this.appUserRepository = appUserRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDTO registerDTO) {
        if (appUserRepository.existsByUsername(registerDTO.getUsername())) {
            return new ResponseEntity<>("Username taken!", HttpStatus.BAD_REQUEST);
        }

        AppUser user = modelMapper.map(registerDTO, AppUser.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role role = roleRepository.findByName("USER").get();

        user.setRoles(Collections.singletonList(role));

        appUserRepository.save(user);

        return new ResponseEntity<>("User created!", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtGenerator.generateToken(authentication);

        return new ResponseEntity<>(new AuthResponseDTO(token), HttpStatus.OK);
    }
}
