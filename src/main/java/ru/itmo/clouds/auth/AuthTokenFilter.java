package ru.itmo.clouds.auth;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.itmo.clouds.service.UserDetailsServiceImpl;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

class AuthTokenFilter extends OncePerRequestFilter {
    private final Logger logger  = LoggerFactory.getLogger(AuthTokenFilter.class);
    private final JwtUtils jwtUtils;
private final UserDetailsServiceImpl userDetailsService;

    AuthTokenFilter(JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @SneakyThrows
    public void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) {

        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                if (username == null)
                    throw new UsernameNotFoundException(username);
                 UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch ( Exception e) {
            logger.error("Cannot set user authentication: "+e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private String parseJwt( HttpServletRequest request)  {
        String headerAuth = request.getHeader("Authorization");
        String prefix = "Bearer ";
        return StringUtils.hasText(headerAuth) && headerAuth.startsWith(prefix)?
            headerAuth.substring(prefix.length()) :null;
    }
}