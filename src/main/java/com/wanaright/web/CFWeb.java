package com.wanaright.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

public class CFWeb {
    private void CFWeb() {
    }

    public static List<Cookie> getCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        return Arrays.asList(cookies);
    }

    public static void setCookie(HttpServletResponse response) {
    }


}
