package com.oocl.workshop.intern.interfaces.api;

import com.oocl.workshop.intern.domain.attendance.service.AttendanceDomService;
import com.oocl.workshop.intern.domain.profile.entity.Intern;
import com.oocl.workshop.intern.domain.profile.entity.Role;
import com.oocl.workshop.intern.domain.profile.entity.User;
import com.oocl.workshop.intern.interfaces.dto.ResultDto;
import com.oocl.workshop.intern.sso.exceptions.SSOException;
import com.oocl.workshop.intern.support.util.UserInfoUtil;
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

import static com.oocl.workshop.intern.sso.util.SSOHelperUtils.getDomainIdFromSSOToken;


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
    public String toAdmin() {
        return "admin";
    }

    @RequestMapping(value = "login")
    public String toNewLogin(ModelMap modelMap,HttpServletRequest request, HttpServletResponse httpResponse) throws SSOException {
        String domainId = getDomainIdFromSSOToken(request, httpResponse);
        modelMap.addAttribute("username", domainId);
        return "login";
    }

    @RequestMapping(value = "/mock/users", method = RequestMethod.GET)
    @ResponseBody
    public ResultDto getUsers() {
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 13; i++) {
            Intern intern = new Intern();
            intern.setDomainId(String.valueOf(i));
            intern.setEmail("847093906@qq.com");
            intern.setName("miaooy2");
            userList.add(intern);
        }
        return ResultDto.success(userList);
    }

    @RequestMapping(value = "/mock/profile/users/{leaderId}", method = RequestMethod.GET)
    @ResponseBody
    public ResultDto users(@PathVariable String leaderId) {
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Intern intern = new Intern();
            intern.setDomainId("User" + String.valueOf(i));
            intern.setEmail("847093906@qq.com");
            userList.add(intern);
        }
        return ResultDto.success(userList);
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
        modelMap.addAttribute("domainId", UserInfoUtil.getUserDetails().getUsername());
        modelMap.addAttribute("teamId", UserInfoUtil.getUserDetails().getTeamId());
    }

}
