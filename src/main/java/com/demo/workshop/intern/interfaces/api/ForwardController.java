package com.demo.workshop.intern.interfaces.api;

import com.demo.workshop.intern.domain.profile.entity.Intern;
import com.demo.workshop.intern.domain.profile.entity.Role;
import com.demo.workshop.intern.domain.profile.entity.User;
import com.demo.workshop.intern.interfaces.dto.ResultDto;
import com.demo.workshop.intern.domain.attendance.service.AttendanceDomService;
import com.demo.workshop.intern.support.util.UserInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * @author MIAOOY2
 */
@Controller
@RequestMapping
public class ForwardController {

    @Autowired
    private AttendanceDomService attendanceDomService;

    @RequestMapping(value = "/intern", method = RequestMethod.GET)
    public String toIntern(ModelMap modelMap) {
        setAttributes(modelMap);
        return "intern";
    }

    @RequestMapping(value = "/leader", method = RequestMethod.GET)
    public String toLeader(ModelMap modelMap) {
        setAttributes(modelMap);
        return "leader";
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String toAdmin(ModelMap modelMap) {
        setAttributes(modelMap);
        return "admin";
    }

    @RequestMapping(value = "login")
    public String toNewLogin(){
        return "login";
    }

    @CrossOrigin
    @RequestMapping(value = "/index")
    public String index(Authentication authentication) {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (roles.contains(Role.SUPER_ADMIN.getFullName())){
            return "redirect:admin";
        }else if (roles.contains(Role.INTERN.getFullName())){
            return "redirect:intern";
        }else if (roles.contains(Role.TEAM_LEADER.getFullName())){
            return "redirect:leader";
        }
        return "error";
    }

    private void setAttributes(ModelMap modelMap) {
        modelMap.addAttribute("domainId", UserInfoUtil.getUserDetails().getDomainId());
        modelMap.addAttribute("teamId", UserInfoUtil.getUserDetails().getTeamId());
        modelMap.addAttribute("userName", UserInfoUtil.getUserDetails().getUsername());
        modelMap.addAttribute("role", UserInfoUtil.getUserDetails().getRoles());
    }

}
