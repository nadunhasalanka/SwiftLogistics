package com.pm.authservice.service;

import com.pm.authservice.dto.SignupRequestDTO;
import com.pm.authservice.model.User;
import com.pm.authservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public void signup(SignupRequestDTO signupRequestDTO) {
        User user = new User();
        user.setEmail(signupRequestDTO.getEmail());
        // check whether the email is already registered or not
        if (userRepository.findByEmail(signupRequestDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email Already in use");
        }
        user.setPassword(signupRequestDTO.getPassword());
        user.setRole(signupRequestDTO.getRole());
        user.setName(signupRequestDTO.getName());
        user.setPassword(passwordEncoder.encode(signupRequestDTO.getPassword()));

        userRepository.save(user);
    }
}
