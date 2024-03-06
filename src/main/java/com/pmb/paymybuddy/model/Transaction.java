package com.pmb.paymybuddy.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "motif")
    private String motif;

    @Column(name = "montant")
    private double montant;

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", date=" + date +
                ", motif='" + motif + '\'' +
                ", montant=" + montant +
                '}';
    }
}
