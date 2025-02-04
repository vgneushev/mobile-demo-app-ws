package com.devdemo.app.ws.io.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;

@Data
@Entity(name = "authorities")
public class AuthorityEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 123654167852L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, length = 20)
    private String name;

    @ManyToMany(mappedBy = "authorities")
    private Collection<RoleEntity> roles;
}
