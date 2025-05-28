package com.picpaydesafio.picpaydesafio.domain.user;

import com.picpaydesafio.picpaydesafio.dtos.user.UserDTO;
import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;


@Entity(name = "users")
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @Column(unique = true)
    private String document;
    @Column(unique = true)
    private String email;
    private String password;
    private BigDecimal balance;
    @Enumerated(EnumType.STRING)
    private UserType userType;

    public User(UserDTO data){
        this.name = data.name();
        this.email = data.email();
        this.password = data.password();
        this.document = data.document();
        this.balance = data.balance();
        this.userType = data.userType();
    }
}
