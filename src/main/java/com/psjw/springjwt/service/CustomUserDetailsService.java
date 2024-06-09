package com.psjw.springjwt.service;

import com.psjw.springjwt.dto.CustomUserDetails;
import com.psjw.springjwt.entity.UserEntity;
import com.psjw.springjwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userData = userRepository.findByUsername(username);
        if(userData != null){
            return new CustomUserDetails(userData);
        }

        return null;
    }
}
