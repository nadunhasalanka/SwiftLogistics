package com.pm.authservice.controller;

import com.pm.authservice.dto.LoginRequestDTO;
import com.pm.authservice.dto.LoginResponseDTO;
import com.pm.authservice.dto.SignupRequestDTO;
import com.pm.authservice.dto.SignupResponseDTO;
import com.pm.authservice.service.AuthService;
import com.pm.authservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @Operation(summary = "Generate token on user Login")
    @PostMapping("/login")
    public ResponseEntity<Optional<LoginResponseDTO>> login(@RequestBody LoginRequestDTO loginRequestDTO){

        Optional<LoginResponseDTO> loginUser = authService.authenticate(loginRequestDTO);

        if(loginUser.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // with the get method we convert the Optional<String> to String
//        String token = loginUser.get().getToken()
//        S
        return ResponseEntity.ok(loginUser);


    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDTO> signup(@RequestBody SignupRequestDTO signupRequestDTO){
        userService.signup(signupRequestDTO);

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setEmail(signupRequestDTO.getEmail());
        loginRequestDTO.setPassword(signupRequestDTO.getPassword());

        ResponseEntity<Optional<LoginResponseDTO>> loginResponseDTOResponseEntity = login(loginRequestDTO);
        String token = loginResponseDTOResponseEntity.getBody().get().getToken();
        String name = loginResponseDTOResponseEntity.getBody().get().getName();
        return ResponseEntity.ok(new SignupResponseDTO(token, name));

    }

    @Operation(summary = "Validate the token")
    @GetMapping("/validate")
    public ResponseEntity<Void> validateToken(@RequestHeader("Authorization") String authHeader){

        // Authorization : Bearer <token>
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return authService.validateToken(authHeader.substring(7))
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
