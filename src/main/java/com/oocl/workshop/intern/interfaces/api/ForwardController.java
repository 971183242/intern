package com.oocl.workshop.intern.interfaces.api;


import com.oocl.workshop.intern.domain.profile.entity.Role;
import com.oocl.workshop.intern.sso.exceptions.SSOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Authenticator;
import java.net.URL;
import java.net.URLConnection;
import java.util.Set;

import static com.oocl.workshop.intern.sso.util.SSOHelperUtils.getDomainIdFromSSOToken;


/**
 * @author MIAOOY2
 */
@Controller
@RequestMapping
public class ForwardController {

    @RequestMapping(value = "/intern", method = RequestMethod.GET)
    public String toIntern() {
        return "intern";
    }

    @RequestMapping(value = "/leader", method = RequestMethod.GET)
    public String toLeader() {
        return "leader";
    }

    /**
     * 进入登录页
     */
    @RequestMapping(value = "login")
    public String toNewLogin(ModelMap modelMap,HttpServletRequest request, HttpServletResponse httpResponse) throws SSOException {
        String domainId = getDomainIdFromSSOToken(request,httpResponse);
        modelMap.addAttribute("username", domainId);
        return "login";
    }

    @CrossOrigin
    @RequestMapping(value = "/index")
    public String index(Authentication authentication) {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (roles.contains(Role.SUPER_ADMIN.getFullName())){
            return "admin";
        }else if (roles.contains(Role.INTERN.getFullName())){
            return "intern";
        }else if (roles.contains(Role.TEAM_LEADER.getFullName())){
            return "leader";
        }
        return "error";
    }
}
