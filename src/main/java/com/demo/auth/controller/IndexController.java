package com.demo.auth.controller;

import com.alibaba.fastjson.JSONObject;
import com.demo.auth.service.AuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @Autowired
    AuthUserService authUserService;

    @GetMapping("/index/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/index/test2")
    public ResponseEntity<?> test2(@RequestBody JSONObject request) {
        System.out.println(request);
        return ResponseEntity.ok().body(request);
    }





}
