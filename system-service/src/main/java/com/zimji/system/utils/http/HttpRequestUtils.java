package com.zimji.system.utils.http;

import io.vertx.core.json.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpMethod;

import java.util.Enumeration;

public class HttpRequestUtils {

    public static String getMethod(HttpServletRequest request) {
        return request.getMethod();
    }

    public static byte[] getBody(HttpServletRequest request) throws Exception {
        byte[] result = new byte[0];
        if (request.getMethod().equalsIgnoreCase(HttpMethod.POST.name())) {
            result = IOUtils.toString(request.getReader()).getBytes();
        }
        return result;
    }

    public static JsonObject getHeaders(HttpServletRequest request) {
        JsonObject headers = new JsonObject();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (ObjectUtils.isNotEmpty(headerNames)) {
            while (headerNames.hasMoreElements()) {
                String header = headerNames.nextElement();
                headers.put(header, request.getHeader(header));
            }
        }
        return headers;
    }

    public static String getReferer(HttpServletRequest request) {
        return request.getHeader("referer");
    }

    public static String getFullURL(HttpServletRequest request) {
        StringBuffer requestURL = request.getRequestURL();
        String queryString = request.getQueryString();
        return ObjectUtils.isEmpty(queryString) ? requestURL.toString() : requestURL.append('?').append(queryString).toString();
    }

    public static String getClientIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ObjectUtils.isEmpty(ip) || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ObjectUtils.isEmpty(ip) || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ObjectUtils.isEmpty(ip) || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ObjectUtils.isEmpty(ip) || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ObjectUtils.isEmpty(ip) || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String getClientOS(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        String lowerCaseUserAgent = userAgent.toLowerCase();

        if (lowerCaseUserAgent.contains("windows")) {
            return "Windows";
        } else if (lowerCaseUserAgent.contains("mac")) {
            return "Mac";
        } else if (lowerCaseUserAgent.contains("x11") || lowerCaseUserAgent.contains("unix")) {
            return "Unix";
        } else if (lowerCaseUserAgent.contains("android")) {
            return "Android";
        } else if (lowerCaseUserAgent.contains("iphone") || lowerCaseUserAgent.contains("ipad")) {
            return "iOS";
        } else {
            return "UnKnown, More-Info: " + userAgent;
        }
    }

    public static String getClientBrowser(HttpServletRequest request) {
        final String browserDetails = getUserAgent(request).toLowerCase();
        String browser = "Unknown";

        if (browserDetails.contains("msie") || browserDetails.contains("rv")) {
            browser = "IE";
        } else if (browserDetails.contains("safari") && browserDetails.contains("version")) {
            browser = "Safari";
        } else if (browserDetails.contains("opr") || browserDetails.contains("opera")) {
            browser = "Opera";
        } else if (browserDetails.contains("chrome")) {
            browser = "Chrome";
        } else if (browserDetails.contains("firefox")) {
            browser = "Firefox";
        } else if (browserDetails.contains("netscape") || browserDetails.contains("mozilla")) {
            browser = "Netscape";
        }

        return browser;
    }

    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

}