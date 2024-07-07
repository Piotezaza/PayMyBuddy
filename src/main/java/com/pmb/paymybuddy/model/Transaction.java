package com.pmb.paymybuddy.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "Transaction")
public class Transaction implements Comparable<Transaction>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "motif")
    private String motif;

    @Column(name = "montant")
    private BigDecimal montant;

    @ManyToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "compte_emetteur", referencedColumnName = "id")
    private ComptePMB issuer;

    @ManyToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "compte_receveur", referencedColumnName = "id")
    private ComptePMB recipient;

    @Column(name = "frais")
    private BigDecimal frais;

    @Override
    public int compareTo(Transaction o) {
        if (date == o.date) return 0;
        if (date.isAfter(o.date)) return 1;
        return -1;
    }
}
