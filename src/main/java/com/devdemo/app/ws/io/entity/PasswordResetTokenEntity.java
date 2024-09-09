package com.devdemo.app.ws.io.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.io.Serial;

@Data
@Entity(name = "password_reset_tokens")
public class PasswordResetTokenEntity {

    @Serial
    private static final long serialVersionUID = 213416785432L;

    @Id
    @GeneratedValue
    private Long id;

    @Nullable
    private String token;

    @OneToOne
    @JoinColumn(name = "users_id")
    private UserEntity userDetails;
}
