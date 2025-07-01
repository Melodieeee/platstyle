package com.example.platstyle.config;

import com.example.platstyle.entities.AppUser;
import com.example.platstyle.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<AppUser> user = userRepository.findByEmail(email);
        user.orElseThrow(() -> new UsernameNotFoundException("Not found:" + email));
        return user.map(MyUserDetail::new).get();
    }
}
