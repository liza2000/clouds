package ru.itmo.clouds.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.itmo.clouds.auth.JwtUtils;
import ru.itmo.clouds.auth.UserDetailsImpl;
import ru.itmo.clouds.entity.EUser;
import ru.itmo.clouds.repository.*;

import javax.persistence.EntityNotFoundException;


@AllArgsConstructor
@NoArgsConstructor
@Data
class RegisterUserRequest{
        String login;
        String password;
}

@AllArgsConstructor
@Data
@NoArgsConstructor
class LoginRequest{
     String login;
    String password;
 }

@AllArgsConstructor
@Data
@NoArgsConstructor
class MessageIdResponse {
    String message;
    Long id;
}


@AllArgsConstructor
@Data
@NoArgsConstructor
class JwtResponse{
        String login;
        String accessToken;
}

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api")
@RestController
@AllArgsConstructor
class UserController {
    private final Logger logger  = LoggerFactory.getLogger(UserController.class);

    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;
    private PasswordEncoder encoder;

    @PostMapping("/signin")
    ResponseEntity<Object> authenticateUser(@RequestBody LoginRequest loginRequest)  {
        Authentication authentication = authenticationManager.authenticate(
           new UsernamePasswordAuthenticationToken(loginRequest.login, loginRequest.password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        logger.error(jwt);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        EUser user = userRepository.findByLogin(userDetails.getUsername()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        return ResponseEntity.ok(new JwtResponse(user.getLogin(), jwt));
    }


    @PostMapping("/register")
    MessageIdResponse  register(@RequestBody RegisterUserRequest payload) {
        logger.error(payload.password);
        if (userRepository.findByLogin(payload.login).isPresent())
            throw new IllegalStateException("User already registered");
        EUser user = new EUser(0L, payload.login, encoder.encode(payload.password));

        user = userRepository.save(user);
        return new MessageIdResponse("Successfully registered user", user.getId());
    }

}