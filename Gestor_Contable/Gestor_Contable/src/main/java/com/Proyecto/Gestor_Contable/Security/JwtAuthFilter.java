package com.Proyecto.Gestor_Contable.Security;

import com.Proyecto.Gestor_Contable.Service.Impl.UsuarioImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil  jwtUtil;
    @Autowired
    private UsuarioImpl  usuario;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null  || !authHeader.startsWith("Bearer")){
            filterChain.doFilter(request,response);
            return;
        }

        String token = authHeader.substring(7);
        String email =jwtUtil.extraerEmail(token);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = usuario.iniciaSesion(email);

            if (jwtUtil.validaToken(token, userDetails)){
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request,response);

    }
}
