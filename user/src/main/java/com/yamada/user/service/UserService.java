package com.yamada.user.service;

import com.yamada.user.form.SigninForm;

public interface UserService {

    String loginByName(String name, String password);

    String loginByEmail(String email, String password);

    void signin(SigninForm signinForm);

    void sendActivationEmail(String email);

    void updatePassword(String activationCode, String email, String password);

    void updateNickName(Integer userId, String nickName);

    void updateEmail(Integer userId, String email, String activationCode);
}
