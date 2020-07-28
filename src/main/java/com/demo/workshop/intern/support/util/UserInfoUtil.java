package com.demo.workshop.intern.support.util;

import com.demo.workshop.intern.interfaces.dto.profile.CurrentUser;
import org.springframework.security.core.context.SecurityContextHolder;


public class UserInfoUtil {
    public static CurrentUser getUserDetails(){
        CurrentUser userInfo;
        try {
             userInfo = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (NullPointerException e){
            userInfo = new CurrentUser("admin");
            return userInfo;
        }
        return userInfo;
    }
}
