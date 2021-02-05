package com.yamada.user.controller;

import cn.hutool.core.lang.Validator;
import com.yamada.user.service.UserService;
import com.yamada.user.form.LoginForm;
import com.yamada.common.utils.ReturnUtil;
import com.yamada.common.vo.ReturnVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "认证")
@RestController
@RequiredArgsConstructor
public class AuthorizationController {

    private final UserService userService;

    @ApiOperation("登录")
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
