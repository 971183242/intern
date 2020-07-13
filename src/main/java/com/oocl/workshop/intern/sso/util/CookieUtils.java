package com.oocl.workshop.intern.sso.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


public class CookieUtils {

    public static Cookie getCookieByName(HttpServletRequest httpRequest, String cookieName) {
        Cookie[] cookies = httpRequest.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookieName.equals(cookies[i].getName())) {
                    return cookies[i];
                }
            }
        }
        return null;
    }

}
