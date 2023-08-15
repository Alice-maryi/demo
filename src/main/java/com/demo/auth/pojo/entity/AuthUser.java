package com.demo.auth.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AuthUser {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    @TableField("username")
    private String username;
    @TableField("password")
    private String password;
    @TableField("enabled")
    private Integer enabled;
    @TableField("account_expired")
    private Integer accountExpired;
    @TableField("account_locked")
    private Integer accountLocked;
    @TableField("credential_expired")
    private Integer credentialExpired;

    @TableField(exist = false)
    private List<Role> roleList = new ArrayList<>();

    public AuthUser() {
        username = "aabb";
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roleList.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public boolean isAccountNonExpired() {
        return Objects.equals(accountExpired, 0);
    }

    public boolean isAccountNonLocked() {
        return Objects.equals(accountLocked, 0);
    }

    public boolean isCredentialsNonExpired() {
        return Objects.equals(credentialExpired, 0);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public Integer getAccountExpired() {
        return accountExpired;
    }

    public void setAccountExpired(Integer accountExpired) {
        this.accountExpired = accountExpired;
    }

    public Integer getAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(Integer accountLocked) {
        this.accountLocked = accountLocked;
    }

    public Integer getCredentialExpired() {
        return credentialExpired;
    }

    public void setCredentialExpired(Integer credentialExpired) {
        this.credentialExpired = credentialExpired;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }


}
