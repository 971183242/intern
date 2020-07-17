package com.oocl.workshop.intern.interfaces.api;

import com.oocl.workshop.intern.domain.attendance.entity.DailyAttendance;
import com.oocl.workshop.intern.domain.attendance.entity.PeriodAttendance;
import com.oocl.workshop.intern.domain.attendance.service.AttendanceDomService;
import com.oocl.workshop.intern.domain.profile.entity.Intern;
import com.oocl.workshop.intern.domain.profile.entity.User;
import com.oocl.workshop.intern.interfaces.dto.ResultDto;
import com.oocl.workshop.intern.interfaces.dto.attendance.AttendanceDTO;
import com.oocl.workshop.intern.sso.exceptions.SSOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    public String toIntern() {
        return "intern";
    }

    @RequestMapping(value = "/leader", method = RequestMethod.GET)
    public String toLeader() {
        return "leader";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public String toLogin(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws SSOException {
        String domainId = getDomainIdFromSSOToken(httpRequest,httpResponse);
        return "domainId "+domainId;
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String toAdmin() {
        return "admin";
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

}
