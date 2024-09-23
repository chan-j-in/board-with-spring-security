package com.board.board.service;

import com.board.board.entity.User;
import com.board.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User create(String username, String email, String password) {
        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .createdDate(LocalDateTime.now())
                .build();
        return userRepository.save(user);
    }

    public User getUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent())
            return user.get();
        throw new UsernameNotFoundException(username);
    }
}
