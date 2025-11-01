package project_management.Server.Component;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import project_management.Server.Service.UserDetailServiceImpl;
import project_management.Server.Util.JwtUtility;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private JwtUtility jwtUtility;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {


        String token=null;
        String email=null;

        if(request.getCookies() != null){
            for(Cookie cookie:request.getCookies()){
                if("jwt".equals(cookie.getName())){
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if(token==null){
            String authHeader=request.getHeader("Authorization");
            if(authHeader !=null && authHeader.startsWith("Bearer ")){
                token=authHeader.substring(7);
            }
        }

        if(token!=null && jwtUtility.validateToken(token)){
            email = jwtUtility.extractEmail(token);
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        chain.doFilter(request, response);
    }
}
