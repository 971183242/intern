package com.oocl.workshop.intern.configuration;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;


@Component("MyAuthenticationSuccessHandler")
public class MyAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        String path = request.getContextPath() ;
        String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
        if (roles.contains("ROLE_ADMIN")){
            response.sendRedirect(basePath+"/admin");
            return;
        }else if (roles.contains("ROLE_INTERN")){
            response.sendRedirect(basePath+"intern");
            return;
        }else if (roles.contains("ROLE_ADMIN")){
            response.sendRedirect(basePath+"leader");
            return;
        }
        response.sendRedirect(basePath+"login");
    }
}

