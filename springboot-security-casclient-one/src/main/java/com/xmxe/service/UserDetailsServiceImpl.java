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
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {//参数s是从cas中获取的用户名,正常逻辑应该拿着参数去数据库查找用户权限
        System.out.println("from csa username ->" + s);
        if("admin".equals(s)){
            return new User(s, "admin", true, true, true, true,
                    AuthorityUtils.createAuthorityList("ROLE_user"));
        }
        return new User(s, "guest", true, true, true, true,
                AuthorityUtils.createAuthorityList("ROLE_guest"));
    }
}
