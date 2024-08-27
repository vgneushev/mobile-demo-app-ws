package com.devdemo.app.ws.io.entity;

import com.devdemo.app.ws.shared.dto.UserDto;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;


@Data
@Entity(name = "addresses")
public class AddressEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 123416785432L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 30)
    private String addressId;

    @Column(nullable = false, length = 60)
    private String city;

    @Column(nullable = false, length = 60)
    private String country;

    @Column(nullable = false, length = 60)
    private String streetName;

    @Column(nullable = false, length = 20)
    private String postalCode;

    @Column(nullable = false)
    private String type;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private UserEntity userDetails;

}