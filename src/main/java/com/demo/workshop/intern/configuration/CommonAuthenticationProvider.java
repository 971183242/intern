package com.demo.workshop.intern.configuration;


import com.demo.workshop.intern.app.service.ProfileAppService;
import com.demo.workshop.intern.domain.profile.entity.Role;
import com.demo.workshop.intern.domain.profile.entity.User;
import com.demo.workshop.intern.interfaces.dto.profile.CurrentUser;
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
        String domainId = authentication.getName();
        String password = (String) authentication.getCredentials();
        if(domainId == null){
            throw new UsernameNotFoundException("用户名为空");
        }
        Set<String> authoritiesSet = new HashSet<>(16);
        Optional<User> userOptional = profileAppService.findUserByDomainId(domainId);
        String teamId = "";
        String username ="";
        if(profileAppService.findTeam(domainId) != null) {
            teamId = profileAppService.findTeam(domainId).getTeamId();
        }
        List<String> roles = new ArrayList<>();
        if(userOptional !=null) {
            for (Role role: userOptional.get().getRoles()) {
                roles.add(role.getFullName());
            }
            username = userOptional.get().getName();
        }
        authoritiesSet.addAll(roles);
        CurrentUser userInfo = new CurrentUser(domainId,username, password, teamId ,authoritiesSet,true, true, true, true);
        userInfo.setAuthorities(authoritiesSet.parallelStream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        return new UsernamePasswordAuthenticationToken(userInfo, password, userInfo.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
