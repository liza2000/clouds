package ru.itmo.clouds.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import ru.itmo.clouds.auth.JwtUtils;
import ru.itmo.clouds.repository.UserRepository;


@Data
@AllArgsConstructor
class AddRequest{
    String name;
    String description;

}



@Controller
@AllArgsConstructor
public class DatasetController {
    private final Logger logger  = LoggerFactory.getLogger(DatasetController.class);

    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;
    private PasswordEncoder encoder;

}
