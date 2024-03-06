package com.pmb.paymybuddy.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER // à la récupération du compte bancaire, tous les virements seront également récupérés
    )
    @JoinColumn(name = "id_compte_bancaire")
    private List<Virement> virements;

    @Override
    public String toString() {
        return "CompteBancaire{" +
                "id=" + id +
                ", iban='" + iban + '\'' +
                ", virements=" + virements +
                '}';
    }
}
