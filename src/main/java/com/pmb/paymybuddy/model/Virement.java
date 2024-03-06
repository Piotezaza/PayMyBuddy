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
@Table(name = "Virement")
public class Virement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "type")
    private String type;

    @Column(name = "montant")
    private double montant;

    @Override
    public String toString() {
        return "Virement{" +
                "id=" + id +
                ", date=" + date +
                ", type='" + type + '\'' +
                ", montant=" + montant +
                '}';
    }
}
