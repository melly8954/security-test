package com.tl.securitytest.domain.user.service;

import com.tl.securitytest.global.auth.dto.JoinDTO;
import com.tl.securitytest.domain.user.entity.UserEntity;
import com.tl.securitytest.domain.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JoinService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void joinProcess(JoinDTO joinDTO) {
        //db에 이미 동일한 username을 가진 회원이 존재하는지?
        boolean isExist = this.userRepository.existsByUsername(joinDTO.getUsername());
        if (isExist) {
            log.error("중복된 회원 존재");
            return;
        }
        UserEntity user = UserEntity.builder()
                .username(joinDTO.getUsername())
                .password(bCryptPasswordEncoder.encode(joinDTO.getPassword()))
                .role("ROLE_USER").build();

        userRepository.save(user);
    }
}
