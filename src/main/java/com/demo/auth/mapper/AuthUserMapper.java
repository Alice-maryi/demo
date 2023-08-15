package com.demo.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.auth.pojo.entity.AuthUser;
import com.demo.auth.pojo.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AuthUserMapper extends BaseMapper<AuthUser> {

    List<Role> listRoleByUserId(@Param("userId") Long userId);

}
