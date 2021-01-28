package com.yamada.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yamada.entity.User;
import com.yamada.exception.AuthException;
import com.yamada.exception.MyException;
import com.yamada.form.SigninForm;
import com.yamada.mapper.UserMapper;
import com.yamada.service.UserService;
import com.yamada.utils.JwtOperator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final JwtOperator jwtOperator;

    @Override
    public String loginByName(String name, String password) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select("id", "name", "password").eq("name", name);
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            throw new AuthException("这个用户名还没注册~");
        }
        if (DigestUtil.bcryptCheck(password, user.getPassword())) {
            // TODO 异步更新用户登录时间
            return generateToken(user);
        } else {
            throw new AuthException("密码不正确~");
        }
    }

    @Override
    public String loginByEmail(String email, String password) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select("id", "name", "password").eq("email", email);
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            throw new AuthException("这个邮箱还没注册~");
        }
        if (DigestUtil.bcryptCheck(password, user.getPassword())) {
            // TODO 异步更新用户登录时间
            return generateToken(user);
        } else {
            throw new AuthException("密码不正确~");
        }
    }

    @Override
    public void signin(SigninForm signinForm) {
        // 1.校验验证码是否正确
        // TODO
        // 2.判断邮箱是否重复
        User user = userMapper.selectOne(new QueryWrapper<User>().select("email").eq("email", signinForm.getMail()));
        if (user != null) {
            throw new AuthException("这个邮箱已经注册了~");
        }
        // 3.判断用户名是否重复
        user = userMapper.selectOne(new QueryWrapper<User>().select("name").eq("name", signinForm.getName()));
        if (user != null) {
            throw new AuthException("这个用户名已经被抢走了，换一个试试吧~");
        }
        // 4.密码加密
        String bcrypt = DigestUtil.bcrypt(signinForm.getPassword());
        // 5.构造user
        user = User.builder()
                .name(signinForm.getName())
                .password(bcrypt)
                .email(signinForm.getMail())
                .build();
        int result = userMapper.insert(user);
        if (result != 0) {
            log.error("【注册】插入数据失败, {}", user.toString());
            throw new MyException("用户注册失败，请稍后再试");
        }
    }

    /**
     * 根据用户信息，调用工具类生成token
     *
     * @param user
     * @return
     */
    private String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("name", user.getName());
        return jwtOperator.generateToken(claims);
    }
}
