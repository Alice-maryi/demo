package com.demo.auth.controller;

import com.alibaba.fastjson.JSONObject;
import com.demo.auth.util.TokenCache;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.logging.log4j.util.Base64Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.demo.auth.constants.CommonConstant.SIGN_KEY;

@RestController
public class AuthController {

    @Autowired
    private UserDetailsService userDetailsService;




    @GetMapping("/auth/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        JSONObject body = new JSONObject() {{
            put("code", 200);
            put("data", null);
        }};
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (userDetails == null) {
            body.put("data", "user not found");
            return ResponseEntity.ok(body);
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            body.put("data", "password is not current");
            return ResponseEntity.ok(body);
        }
        String token = generateToken(userDetails);
        body.put("data", token);
        return ResponseEntity.ok(body);
    }

    private String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorityList", userDetails.getAuthorities());
        String token = doGenerateToken(claims, userDetails.getUsername());
        TokenCache.putToken(userDetails.getUsername(), token);
        return token;
    }

    // 生成token
    private String doGenerateToken(Map<String, Object> claims, String username) {
        Date expirationDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60);
        return Jwts.builder().addClaims(claims).setSubject(username).setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, Base64Util.encode(SIGN_KEY)).compact();
    }

    @GetMapping("/auth/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        removeAuthorization(request);
        return ResponseEntity.ok("logout success !!!");
    }

    private void removeAuthorization(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        Claims claims = Jwts.parser().setSigningKey(Base64Util.encode(SIGN_KEY)).parseClaimsJws(token).getBody();
        TokenCache.removeToken(claims.getSubject());
    }
}
