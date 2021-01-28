package com.yamada.controller;

import com.yamada.form.SigninForm;
import com.yamada.service.UserService;
import com.yamada.utils.ReturnUtil;
import com.yamada.vo.ReturnVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ReturnVO> signin(@RequestBody @Validated SigninForm signinForm) {
        userService.signin(signinForm);
        return ReturnUtil.success(null);
    }
}
