package ru.itmo.clouds.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.itmo.clouds.auth.UserDetailsImpl;
import ru.itmo.clouds.repository.EUser;
import ru.itmo.clouds.repository.UserRepository;

@Service
@AllArgsConstructor
@NoArgsConstructor
public
class UserService {
    private  UserRepository userRepository;

    Long getCurrentUserId() {
       return  ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
    }

    EUser getUserFromAuth() {
       return userRepository.findById(getCurrentUserId()).orElseThrow(()->
          new UsernameNotFoundException("User not found - " + getCurrentUserId()));
    }

}