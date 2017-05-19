package com.cprt.demo.http.request.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import com.cprt.demo.http.request.wrapper.ParameterRequestWrapper;

public class RequestParameterSpaceFilter extends OncePerRequestFilter {

    @Override  
    protected void doFilterInternal(HttpServletRequest request,  
            HttpServletResponse response, FilterChain filterChain)  
            throws ServletException, IOException {  
            ParameterRequestWrapper requestWrapper = new ParameterRequestWrapper(request);  
            filterChain.doFilter(requestWrapper, response);  
    } 
}
