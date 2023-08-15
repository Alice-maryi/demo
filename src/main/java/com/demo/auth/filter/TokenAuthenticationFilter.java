package com.demo.auth.filter;

import com.demo.auth.util.TokenCache;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Base64Util;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static com.demo.auth.constants.CommonConstant.SIGN_KEY;

@Slf4j
public class TokenAuthenticationFilter extends BasicAuthenticationFilter {

    private UserDetailsService userDetailsService;

    public TokenAuthenticationFilter(AuthenticationManager authenticationManager, UserDetailsService userDetailsService) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

//        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(request);
        // jwt verify success
//        if (authenticationToken != null) {
//            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//        }
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("Authorization : {}", token);
        if (token ==null) {
            return null;
        }
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(Base64Util.encode(SIGN_KEY)).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            return null;
        }
        // 提取token里边的username，验证有没有这个用户
        String username = claims.getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        // 有这个用户而且token验证通过的话，返回用户认证信息
        // token是代替密码的一种方式，所以token里边不需要密码，token在发行的时候会验证密码，所以某种程度上说，token是密码的替代品
        if (userDetails != null && validateToken(token)) {
            // 这里构造的时候需要加上权限列表，不然该用户没有权限访问任何接口
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(userDetails.getAuthorities());
            return authenticationToken;
        }
        return null;
    }

    private boolean validateToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(Base64Util.encode(SIGN_KEY)).parseClaimsJws(token).getBody();
        String token1 = TokenCache.getToken(claims.getSubject());
        return Objects.equals(token, token1);
    }
}
