package com.oocl.workshop.intern.interfaces.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

}
