package com.pmb.paymybuddy.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Compte_bancaire")
public class CompteBancaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "iban")
    private String iban;

    @OneToMany(mappedBy = "compteBancaire")
    private List<Virement> virements;

    @Override
    public String toString() {
        return "CompteBancaire{" +
                "id=" + id +
                ", iban='" + iban + '\'' +
                ", virements=" + virements +
                '}';
    }

    public BigDecimal getBalance() {
        BigDecimal balance = BigDecimal.ZERO;
        for (Virement virement : virements) {
            balance = virement.getType().equals("IN") ? balance.add(new BigDecimal(String.valueOf(virement.getMontant()))) : balance.subtract(new BigDecimal(String.valueOf(virement.getMontant())));
        }
        return balance;
    }
}
