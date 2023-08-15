package com.demo.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.auth.mapper.AuthUserMapper;
import com.demo.auth.pojo.entity.AuthUser;
import com.demo.auth.pojo.entity.Role;
import com.demo.auth.service.AuthUserService;
import com.demo.auth.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthUserServiceImpl extends ServiceImpl<AuthUserMapper, AuthUser> implements UserDetailsService, AuthUserService {

    @Autowired
    RoleService roleService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser user = getOne(new LambdaQueryWrapper<AuthUser>().eq(AuthUser::getUsername, username));
        if (user == null) {
            return null;
        }
        List<Role> roleList = baseMapper.listRoleByUserId(user.getId());
        user.setRoleList(roleList);
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void test() {
        Role role = new Role();
        role.setName("ttest");
        roleService.savee(role);
        tt();
    }


    private void tt() {
        AuthUser authUser = new AuthUser();
        authUser.setUsername("i");
        authUser.setPassword("bb");
        this.save(authUser);
        int i = 1 / 0;
    }
}
