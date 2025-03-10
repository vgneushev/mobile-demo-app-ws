package com.devdemo.app.ws.io.entity;

import lombok.*;
import org.springframework.lang.Nullable;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;

@Data
@Entity(name = "users")
public class UserEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 123456785432L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, length = 120, unique = true)
    private String email;

    @Column(nullable = false)
    private String encryptedPassword;

    @Nullable
    private String emailVerificationToken;

    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(name="users_roles",
            joinColumns = @JoinColumn(name = "users_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "roles_id", referencedColumnName = "id"))
    private Collection<RoleEntity> roles;

    @Column(nullable = false)
    private Boolean emailVerified = false;

    @OneToMany(mappedBy = "userDetails", cascade = CascadeType.ALL)
    private Collection<AddressEntity> addresses;

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", encryptedPassword='" + encryptedPassword + '\'' +
                ", emailVerificationToken='" + emailVerificationToken + '\'' +
                ", emailVerified=" + emailVerified +
                ", addresses=" + addresses +
                '}';
    }
}
