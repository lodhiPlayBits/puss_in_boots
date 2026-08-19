package com.CurdDemo.curdDemo.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.logging.LogRecord;

@Component
public class LoggingFilter implements Filter {


    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {
        System.out.println("Request Entered In logging Filter");
        HttpServletRequest httpServletRequest=(HttpServletRequest) request;
        System.out.println(httpServletRequest.getPathInfo()+" "+httpServletRequest.getMethod());
        HttpServletResponse httpServletResponse=(HttpServletResponse) response;
        chain.doFilter(request,response);
        System.out.println("Response Leave out logging filter");
        System.out.println(httpServletResponse.getStatus()+" "+httpServletResponse.getOutputStream());

    }
}
