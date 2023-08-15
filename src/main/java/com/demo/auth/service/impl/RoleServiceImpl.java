package com.demo.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.auth.mapper.RoleMapper;
import com.demo.auth.pojo.entity.Role;
import com.demo.auth.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper,Role> implements RoleService {

    @Override
    public void savee(Role role) {
        this.save(role);
    }
}
