package com.yamada.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorizationController {

    @PostMapping("/login")
    public void login(String name, String password) {

    }
}
