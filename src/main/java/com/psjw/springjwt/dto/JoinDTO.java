package com.psjw.springjwt.dto;

import com.psjw.springjwt.entity.UserEntity;


public record JoinDTO (
        String username,
        String password
) {
    public UserEntity toEntity(){
        return UserEntity.builder()
                .username(username)
                .password(password)
                .role("ROLE_ADMIN")
                .build();
    }
}
