package ru.itmo.clouds.service;

import lombok.AllArgsConstructor;
import ru.itmo.clouds.auth.UserDetailsImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.itmo.clouds.repository.UserRepository;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    UserRepository userRepository;


     public UserDetails loadUserByUsername(String username) {
        return UserDetailsImpl.build(userRepository.findByLogin(username)
                 .orElseThrow(() -> new
             UsernameNotFoundException("Username not found - " + username))
         );
     }
}