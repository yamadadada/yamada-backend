package com.yamada.user.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.extra.mail.MailUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yamada.common.constant.RedisConstant;
import com.yamada.user.entity.User;
import com.yamada.common.exception.AuthException;
import com.yamada.common.exception.MyException;
import com.yamada.user.form.SigninForm;
import com.yamada.user.mapper.UserMapper;
import com.yamada.user.service.UserService;
import com.yamada.common.utils.JwtOperator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final JwtOperator jwtOperator;

    private final StringRedisTemplate stringRedisTemplate;

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
        String code = stringRedisTemplate.opsForValue().get(RedisConstant.ACTIVATION_CODE_PREFIX + signinForm.getMail());
        if (code == null) {
            throw new MyException("验证码已过期，请重新发送");
        }
        if (!code.equals(signinForm.getActivationCode())) {
            throw new MyException("验证码不正确~");
        }
        stringRedisTemplate.delete(RedisConstant.ACTIVATION_CODE_PREFIX + signinForm.getMail());

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

    @Override
    public void sendActivationEmail(String email) {
        // 1.检查是否重复发送
        if (stringRedisTemplate.opsForValue().get(RedisConstant.ACTIVATION_CODE_INTERVAL_PREFIX + email) != null) {
            throw new MyException("验证码不要重复发送");
        }

        // 2.生成随机验证码
        String code = RandomUtil.randomNumbers(6);

        // 3.放入redis缓存
        // 验证码有效期为1小时
        stringRedisTemplate.opsForValue().set(RedisConstant.ACTIVATION_CODE_PREFIX + email, code, 1, TimeUnit.HOURS);
        // 验证码发送间隔为60秒
        stringRedisTemplate.opsForValue().set(RedisConstant.ACTIVATION_CODE_INTERVAL_PREFIX + email, code, 60, TimeUnit.SECONDS);

        // 4.发送邮件
        MailUtil.send(email, "注册账号-验证码", "宁的契约验证码：" + code + "，记得在1小时内完成注册哦~", false);
    }

    /**
     * 根据用户信息，调用工具类生成token
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
