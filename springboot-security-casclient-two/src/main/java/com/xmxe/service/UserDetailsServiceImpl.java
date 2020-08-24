package com.xmxe.service;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetail")
public class UserDetailsServiceImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {//参数s是从cas中获取的用户名,正常的逻辑是拿着用户名从数据库查询用户的权限
        System.out.println("client2用户名->" + s);
        if("admin".equals(s)){
            return new User(s, "admin", true, true, true, true,
                    AuthorityUtils.createAuthorityList("ROLE_user"));
        }
        return new User(s, "test", true, true, true, true,
                AuthorityUtils.createAuthorityList("ROLE_guest"));

    }
}
