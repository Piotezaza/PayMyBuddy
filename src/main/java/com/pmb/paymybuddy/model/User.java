package com.pmb.paymybuddy.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Utilisateur")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "prenom")
    private String prenom;

    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "id")
    private ComptePMB compte_pmb;

    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "id")
    private CompteBancaire compte_bancaire;

    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            }
    )
    @JoinTable(
            name = "contact",
            joinColumns = @JoinColumn(name = "utilisateur"),
            inverseJoinColumns = @JoinColumn(name = "contact")
    )
    private List<User> contacts = new ArrayList<>();

    @Override
    public String toString() {
        return "Utilisateur {" +
                "id=" + id +
                ", prenom='" + prenom + '\'' +
                ", compte PMB nº" + compte_pmb.getId() +
                ", compte bancaire: IBAN = " + (compte_bancaire == null ? "compte bancaire non défini" : compte_bancaire.getIban()) +
                ", contacts=" + contacts +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User)) return false;
        // TODO : remplacer prenom par email par la suite
        return this.prenom.equals(((User) obj).prenom);
    }
}
