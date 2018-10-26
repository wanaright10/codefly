package code.fly.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class CFWeb {
    private void CFWeb() {
    }

    public static List<Cookie> getCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        return Arrays.asList(cookies);
    }

    public static void setCookie(HttpServletResponse response, String name, String value) {
        Cookie cookie = new Cookie(name, value);
        response.addCookie(cookie);
    }

    public static String getCookie(HttpServletRequest request, String name) {
        Cookie cookie = Stream.of(request.getCookies())
                .filter(item -> name.equals(item.getName()))
                .findFirst()
                .orElse(null);

        return cookie == null ? "" : cookie.getValue();
    }
}
