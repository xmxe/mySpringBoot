package com.xmxe;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomEncoderPassword implements PasswordEncoder {
    //建议使用security 里的加密
    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public String encode(CharSequence textPassword) {
        String encode = passwordEncoder.encode(textPassword);
        System.out.println("明文：" + textPassword + "  加过密后的密文：" + encode);
        return encode;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
        System.out.println("密码比对结果：" + matches);
        return matches;
    }
}
