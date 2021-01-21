package com.yamada.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginForm {

    @NotEmpty(message = "名称/邮箱不能为空")
    private String name;

    @NotEmpty(message = "密码不能为空")
    private String password;
}
