package com.yamada.service;

import com.yamada.form.SigninForm;

public interface UserService {

    String loginByName(String name, String password);

    String loginByEmail(String email, String password);

    void signin(SigninForm signinForm);
}
