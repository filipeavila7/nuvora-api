package com.nuvora.nuvora.profile.entity;


import com.nuvora.nuvora.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String profileUrlImg;

    @Column
    private String cpf;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
