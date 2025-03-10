package com.devdemo.app.ws.security;

import com.devdemo.app.ws.io.entity.AuthorityEntity;
import com.devdemo.app.ws.io.entity.RoleEntity;
import com.devdemo.app.ws.io.entity.UserEntity;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Data
public class UserPrincipal implements UserDetails {

    @Serial
    private static final long serialVersionUID = 12345622785432L;

    private UserEntity userEntity;

    private String id;

    public UserPrincipal(UserEntity userEntity) {
        this.userEntity = userEntity;
        this.id = userEntity.getUserId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new HashSet<>();
        Collection<AuthorityEntity> authorityEntities = new HashSet();

        Collection<RoleEntity> roleEntities = userEntity.getRoles();

        if (roleEntities == null) return authorities;

        roleEntities.forEach(roleEntity -> {
            authorities.add(new SimpleGrantedAuthority(roleEntity.getName()));
            authorityEntities.addAll(roleEntity.getAuthorities());
        });

        authorityEntities.forEach(authorityEntity -> {
            authorities.add(new SimpleGrantedAuthority(authorityEntity.getName()));
        });

        return authorities;
    }

    @Override
    public String getPassword() {
        return this.userEntity.getEncryptedPassword();
    }

    @Override
    public String getUsername() {
        return this.userEntity.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return this.userEntity.getEmailVerified();
    }
}
