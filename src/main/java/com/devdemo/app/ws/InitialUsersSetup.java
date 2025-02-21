package com.devdemo.app.ws;

import com.devdemo.app.ws.io.entity.AuthorityEntity;
import com.devdemo.app.ws.io.entity.RoleEntity;
import com.devdemo.app.ws.io.entity.UserEntity;
import com.devdemo.app.ws.repository.AuthorityRepository;
import com.devdemo.app.ws.repository.RoleRepository;
import com.devdemo.app.ws.repository.UserRepository;
import com.devdemo.app.ws.shared.util.Util;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class InitialUsersSetup {

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Autowired
    UserRepository userRepository;

    @EventListener
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {
        final AuthorityEntity readAuthority = createAuthority("READ_AUTHORITY");
        final AuthorityEntity writeAuthority = createAuthority("WRITE_AUTHORITY");
        final AuthorityEntity deleteAuthority = createAuthority("DELETE_AUTHORITY");

        final RoleEntity adminRole = createRole("ROLE_ADMIN", List.of(readAuthority, writeAuthority, deleteAuthority));
        final RoleEntity userRole = createRole("ROLE_USER", List.of(readAuthority, writeAuthority));

        UserEntity adminUser = new UserEntity();
        adminUser.setFirstName("Vlad");
        adminUser.setLastName("Admin");
        adminUser.setEmail("vlad@admin.com");
        adminUser.setEmailVerified(Boolean.TRUE);
        adminUser.setUserId(Util.generateUserId());
        adminUser.setEncryptedPassword(encoder.encode("1234"));
        adminUser.setRoles(List.of(adminRole));
        userRepository.save(adminUser);
    }

    @Transactional
    private AuthorityEntity createAuthority(@NonNull final String name) {
        AuthorityEntity authorityEntity = authorityRepository.findByName(name);
        if (authorityEntity == null) {
            authorityEntity = new AuthorityEntity(name);
            authorityRepository.save(authorityEntity);
        }
        return authorityEntity;
    }

    @Transactional
    private RoleEntity createRole(@NonNull final String name, @NonNull final Collection<AuthorityEntity> authorities) {
        RoleEntity role = roleRepository.findByName(name);
        if (role == null) {
            role = new RoleEntity(name);
            role.setAuthorities(authorities);
            roleRepository.save(role);
        }
        return role;
    }
}
