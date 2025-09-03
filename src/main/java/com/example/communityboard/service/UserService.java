package com.example.communityboard.service;

import com.example.communityboard.domain.User;
import com.example.communityboard.repository.UserRepository;
import com.example.communityboard.domain.User.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long registerNewUser(String username, String password, String email) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalStateException("Username already exists");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalStateException("Email already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password)); // 비밀번호 암호화
        user.setEmail(email);
        user.setRole(Role.USER); // 기본 역할은 USER

        userRepository.save(user);
        return user.getId();
    }
}



