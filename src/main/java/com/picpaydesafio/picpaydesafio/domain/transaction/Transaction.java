package com.picpaydesafio.picpaydesafio.domain.transaction;


import com.picpaydesafio.picpaydesafio.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "transactions")
@Table(name = "transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private BigDecimal amount;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private User sender;
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private User receiver;
    private LocalDateTime timestamp;
}
