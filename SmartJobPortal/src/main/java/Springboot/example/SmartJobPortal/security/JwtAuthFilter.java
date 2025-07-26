package Springboot.example.SmartJobPortal.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String token;
        final String email;
        final String role;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("❌ Missing or invalid Authorization header");
            filterChain.doFilter(request, response);
            return;
        }

        token = authHeader.substring(7);
        email = jwtService.extractUsername(token);
        role = jwtService.extractRole(token);

        System.out.println("🔐 Token: " + token);
        System.out.println("📧 Email: " + email);
        System.out.println("🎭 Role: " + role);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = null;

            // Load user details based on role
            if ("ROLE_ADMIN".equals(role)) {
                userDetails = customUserDetailsService.loadAdminByUsername(email);
            } else if ("ROLE_USER".equals(role)) {
                userDetails = customUserDetailsService.loadUserByUsername(email);
            }

            if (userDetails != null && jwtService.isTokenValid(token, userDetails)) {
                System.out.println("✅ Loaded UserDetails: " + userDetails.getUsername());

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

                System.out.println("🔓 SecurityContext set for: " + email);
            } else {
                System.out.println("❌ Invalid token or user not found");
            }
        }

        filterChain.doFilter(request, response);
    }
}
