package com.yamada.user.controller;

import cn.hutool.core.lang.Validator;
import com.yamada.common.auth.CheckLogin;
import com.yamada.common.exception.MyException;
import com.yamada.user.form.SigninForm;
import com.yamada.user.service.UserService;
import com.yamada.common.utils.ReturnUtil;
import com.yamada.common.vo.ReturnVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Api(tags = "用户")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ApiOperation("注册")
    @PostMapping
    public ResponseEntity<ReturnVO> signin(@RequestBody @Validated SigninForm signinForm) {
        userService.signin(signinForm);
        return ReturnUtil.success(null);
    }

    @ApiOperation("发送验证码")
    @GetMapping("/activation-code")
    public ResponseEntity<ReturnVO> activationCode(@RequestParam("mail") @Email(message = "邮箱格式不正确")
                                                       @Range(max = 20, message = "邮箱长度太长") String mail) {
        userService.sendActivationEmail(mail);
        return ReturnUtil.success(null);
    }

    @ApiOperation("修改密码")
    @PutMapping("/password")
    public ResponseEntity<ReturnVO> updatePassword(@RequestParam("activationCode") String activationCode,
                                                   @RequestParam("mail") @Email(message = "邮箱格式不正确")
                                                       @Range(max = 20, message = "邮箱长度太长") String mail,
                                                   @RequestParam("password")
                                                       @Range(min = 6, max = 20, message = "密码长度必须在6-20之间") String password) {
        if (activationCode.length() != 6 || !Validator.isNumber(activationCode)) {
            throw new MyException("验证码格式不正确");
        }

        userService.updatePassword(activationCode, mail, password);

        return ReturnUtil.success(null);
    }

    @ApiOperation("修改昵称")
    @PostMapping("/nickname")
    @CheckLogin
    public ResponseEntity<ReturnVO> updateName(@RequestParam("nickName") @Range(max = 20, message = "昵称不能超过20位") String nickName,
                                               HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("id");
        userService.updateNickName(userId, nickName);

        return ReturnUtil.success("修改昵称成功！", null);
    }
}
