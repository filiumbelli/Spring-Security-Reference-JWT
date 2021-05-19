package com.sofisticat.security.jwt;

import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtTokenVerifier extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;
    private final JwtSecretKey jwtSecretKey;


    public JwtTokenVerifier(JwtConfig jwtConfig, JwtSecretKey jwtSecretKey) {
        this.jwtConfig = jwtConfig;
        this.jwtSecretKey = jwtSecretKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (Strings.isNullOrEmpty(authorization) || !authorization.startsWith(jwtConfig.getTokenPrefix())) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            String token = authorization.replace(jwtConfig.getTokenPrefix(), "");
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(jwtSecretKey.getSecretKey())
                    .build()
                    .parseClaimsJws(token);
            String username = claimsJws.getBody().getSubject();
            var authorities = (List<Map<String, String>>) claimsJws.getBody().get("authorities");

            Set<SimpleGrantedAuthority> authority = authorities.stream()
                    .map(i -> new SimpleGrantedAuthority(i.get("authority")))
                    .collect(Collectors.toSet());
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    authority
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        filterChain.doFilter(request, response);
    }
}
