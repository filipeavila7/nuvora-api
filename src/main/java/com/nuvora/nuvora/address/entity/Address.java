package com.nuvora.nuvora.address.entity;

import com.nuvora.nuvora.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;


    private String street;
    private String number;
    private String neighborhood;
    private String city;
    private String state;
    private String zipCode;
}