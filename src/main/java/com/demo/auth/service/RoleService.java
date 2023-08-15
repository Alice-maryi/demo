package com.demo.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.auth.pojo.entity.Role;

public interface RoleService extends IService<Role> {

    void savee(Role role);
}
