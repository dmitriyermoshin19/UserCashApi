package com.example.usercashapi.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.header.HeaderWriterFilter;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class UserFilterForDelete implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest httpRequest  = (HttpServletRequest)servletRequest;
            if(httpRequest.getRequestURI().startsWith("/user/admin/") && httpRequest.getMethod().equals("DELETE")) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                Set<String> roles = authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
                if(roles.contains("admin")) {
                    filterChain.doFilter(servletRequest, servletResponse);
                } else {
                  log.warn("User can not delete...");
                    ResponseEntity.badRequest();
                }
                System.out.println("---");
            }
        }
    }
}
