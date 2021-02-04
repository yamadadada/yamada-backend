package com.yamada.user.form;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class SigninForm {

    @NotBlank(message = "用户名不能为空")
    @Range(max = 50, message = "用户名不能太长")
    private String name;

    @NotBlank(message = "密码不能为空")
    @Range(min = 6, max = 50, message = "密码长度必须在6-50之间")
    private String password;

    @NotBlank(message = "验证码不能为空")
    private String activationCode;

    @Email(message = "请输入正确的邮箱格式")
    private String mail;
}
