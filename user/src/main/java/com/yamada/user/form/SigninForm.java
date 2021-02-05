package com.yamada.user.form;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;

@Data
public class SigninForm {

    @Range(max = 20, message = "用户名不能太长")
    private String name;

    @Range(min = 6, max = 20, message = "密码长度必须在6-50之间")
    private String password;

    @Range(min = 6, max = 6, message = "验证码长度必须为6位")
    private String activationCode;

    @Email(message = "请输入正确的邮箱格式")
    @Range(max = 20, message = "邮箱长度太长")
    private String mail;
}
