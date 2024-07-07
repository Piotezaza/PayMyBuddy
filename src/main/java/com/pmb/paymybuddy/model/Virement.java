package com.pmb.paymybuddy.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
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
    private BigDecimal montant;

    @ManyToOne
    @JoinColumn(name = "id_compte_pmb")
    private ComptePMB comptePMB;

    @ManyToOne
    @JoinColumn(name = "id_compte_bancaire")
    private CompteBancaire compteBancaire;

    @Override
    public String toString() {
        return "Virement{" +
                "id=" + id +
                ", date=" + date +
                ", type='" + type + '\'' +
                ", montant=" + montant +
                ", comptePMB=" + comptePMB +
                ", compteBancaire=" + compteBancaire +
                '}';
    }
}
