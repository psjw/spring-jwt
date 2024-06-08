package com.psjw.springjwt.service;

import com.psjw.springjwt.dto.JoinDTO;
import com.psjw.springjwt.entity.UserEntity;
import com.psjw.springjwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void  joinProcess(JoinDTO joinDTO){
        String username = joinDTO.username();

        Boolean isExist = userRepository.existsByUsername(username);

        if(isExist){
            return;
        }

        UserEntity user = joinDTO.toEntity();
        user.encodePassword(bCryptPasswordEncoder);
        userRepository.save(user);
    }
}
