package com.uce.notes.Services.ServicesImp;

import com.uce.notes.Model.User;
import com.uce.notes.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailServiceImp implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        user.getRols()
                .forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(
                        role.getName()))));

        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                user.isAccountNoExpired(),
                user.isCredentialNoExpired(),
                user.isAccountNoLocked(),
                authorityList);
    }
}

