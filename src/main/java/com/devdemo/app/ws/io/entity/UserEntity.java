package com.devdemo.app.ws.io.entity;

import lombok.*;
import org.springframework.lang.Nullable;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Entity(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@ToString
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

    @Column(nullable = false)
    private Boolean emailVerified = false;
}
