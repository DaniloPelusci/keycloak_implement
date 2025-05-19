package com.dan.filter;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.springframework.stereotype.Component;

import com.dan.auth.KeycloakTokenUtil;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter implements Filter {

    private final KeycloakTokenUtil tokenUtil;

    public JwtAuthFilter(KeycloakTokenUtil tokenUtil) {
        this.tokenUtil = tokenUtil;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String authHeader = req.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // remove "Bearer "

            try {
                String userId = tokenUtil.getUserId(token);
                String username = tokenUtil.getUsername(token);
                List<String> roles = tokenUtil.getRoles(token);

                req.setAttribute("userId", userId);
                req.setAttribute("username", username);
                req.setAttribute("roles", roles);

                chain.doFilter(req, res);
                return;

            } catch (Exception e) {
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                res.getWriter().write("Token inválido ou não autorizado.");
                return;
            }

        }

        // Se não houver token, também bloqueia
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.getWriter().write("Token ausente.");
    }
}
