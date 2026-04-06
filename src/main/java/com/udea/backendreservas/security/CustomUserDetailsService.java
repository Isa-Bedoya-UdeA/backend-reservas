package com.udea.backendreservas.security;

import com.udea.backendreservas.entity.User;
import com.udea.backendreservas.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getTipoUsuario().name());

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPasswordHash())
                .authorities(Collections.singletonList(authority))
                .accountExpired(false)
                .accountLocked(user.getBloqueadoHasta() != null && user.getBloqueadoHasta().isAfter(java.time.LocalDateTime.now()))
                .credentialsExpired(false)
                .disabled(!user.getEstado())
                .build();
    }
}
