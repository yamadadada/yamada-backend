package com.yamada.controller;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ReUtil;
import com.yamada.form.LoginForm;
import com.yamada.service.UserService;
import com.yamada.utils.ReturnUtil;
import com.yamada.vo.ReturnVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthorizationController {

    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ReturnVO> login(@RequestBody @Validated LoginForm loginForm) {
        // 1.判断name是否为邮箱
        boolean isEmail = Validator.isEmail(loginForm.getName());
        String token;
        if (isEmail) {
            token = userService.loginByEmail(loginForm.getName(), loginForm.getPassword());
        } else {
            token = userService.loginByName(loginForm.getName(), loginForm.getPassword());
        }
        return ReturnUtil.success(token);
    }
}
