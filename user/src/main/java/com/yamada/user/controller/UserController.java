package com.yamada.user.controller;

import cn.hutool.core.lang.Validator;
import com.yamada.common.exception.MyException;
import com.yamada.user.form.SigninForm;
import com.yamada.user.service.UserService;
import com.yamada.common.utils.ReturnUtil;
import com.yamada.common.vo.ReturnVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/activation-code")
    public ResponseEntity<ReturnVO> activationCode(@RequestParam("mail") String mail) {
        if (!Validator.isEmail(mail)) {
            throw new MyException("邮箱格式不正确");
        }
        userService.sendActivationEmail(mail);
        return ReturnUtil.success(null);
    }
}
