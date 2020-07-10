package com.oocl.workshop.intern.interfaces.api;


import com.oocl.workshop.intern.sso.exceptions.SSOException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public String toLogin(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws SSOException {
        String domainId = getDomainIdFromSSOToken(httpRequest,httpResponse);
        return "domainId "+domainId;
    }

}
