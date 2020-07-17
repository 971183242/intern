package com.oocl.workshop.intern.configuration;


import com.oocl.workshop.intern.app.service.ProfileAppService;
import com.oocl.workshop.intern.domain.profile.entity.Role;
import com.oocl.workshop.intern.domain.profile.entity.User;
import com.oocl.workshop.intern.interfaces.dto.profile.CurrentUser;
import com.oocl.workshop.intern.interfaces.dto.profile.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CommonAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private ProfileAppService profileAppService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        if(username == null){
            throw new UsernameNotFoundException("用户名为空");
        }
        Set<String> authoritiesSet = new HashSet<>(16);
        Optional<User> userOptional = profileAppService.findUserByDomainId(username);
        List<String> roles = new ArrayList<>();
        if(userOptional.get().getRoles() !=null) {
            for (Role role: userOptional.get().getRoles()) {
                roles.add(role.getFullName());
            }
        }
        authoritiesSet.addAll(roles);
        CurrentUser userInfo = new CurrentUser(username, password, authoritiesSet,true, true, true, true);
        userInfo.setAuthorities(authoritiesSet.parallelStream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        return new UsernamePasswordAuthenticationToken(userInfo, password, userInfo.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
