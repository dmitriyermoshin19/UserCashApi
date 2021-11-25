package com.example.usercashapi.servises;

import com.example.usercashapi.domains.Role;
import com.example.usercashapi.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

@Service
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        var userByName = userRepository.findByName(name)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User Not Found with username: " + name));

        return new User(userByName.getName(), userByName.getPassword(),
                mapRolesToAuthorities(userByName.getRole()));
    }
    private Collection<GrantedAuthority> mapRolesToAuthorities(Role role) {
        List<Role> collections = new ArrayList<>();
        collections.add(role);
        return collections.stream().map(r -> new SimpleGrantedAuthority(
                r.getName())).collect(Collectors.toList());
    }
}
