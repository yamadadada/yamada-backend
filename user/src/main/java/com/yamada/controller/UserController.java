package com.yamada.controller;

import cn.hutool.core.lang.Validator;
import com.yamada.exception.MyException;
import com.yamada.form.SigninForm;
import com.yamada.service.UserService;
import com.yamada.utils.ReturnUtil;
import com.yamada.vo.ReturnVO;
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
    }
}
