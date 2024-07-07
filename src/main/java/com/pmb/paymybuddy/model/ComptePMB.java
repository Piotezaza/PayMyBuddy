package com.pmb.paymybuddy.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "Compte_PMB")
public class ComptePMB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "compte_emetteur")
    private List<Transaction> debits;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "compte_receveur")
    private List<Transaction> credits;

    @OneToMany(mappedBy = "comptePMB")
    private List<Virement> virements;

    @OneToOne(mappedBy = "comptePMB", optional = false)
    private User user;

    public BigDecimal getBalance() {
        BigDecimal balance = BigDecimal.ZERO;

        BigDecimal totalDebits = debits.stream()
                .map(transaction -> transaction.getMontant().add(transaction.getFrais()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCredits = credits.stream()
                .reduce(BigDecimal.ZERO, (subtotal, element) -> subtotal.add(element.getMontant()), BigDecimal::add);

        return balance.add(totalCredits).subtract(totalDebits);
    }
}