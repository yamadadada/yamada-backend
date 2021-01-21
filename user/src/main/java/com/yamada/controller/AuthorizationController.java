package com.yamada.controller;

import cn.hutool.core.util.ReUtil;
import com.yamada.form.LoginForm;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorizationController {

    @PostMapping("/login")
    public void login(@RequestBody @Validated LoginForm loginForm) {
        // 1.判断name是否为邮箱
        boolean isEmail = ReUtil.isMatch("^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", loginForm.getName());
        if (isEmail) {

        }
    }
}
