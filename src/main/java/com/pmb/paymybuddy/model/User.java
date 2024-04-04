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
    private int id;

    @Column(name = "prenom", nullable = false)
    private String prenom;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "mot_de_passe" , nullable = false)
    private String password;

    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "id_compte_pmb", referencedColumnName = "id")
    private ComptePMB comptePMB;

    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "id_compte_bancaire", referencedColumnName = "id")
    private CompteBancaire compteBancaire;

    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            }
    )
    @JoinTable(
            name = "contact",
            joinColumns = @JoinColumn(name = "user"),
            inverseJoinColumns = @JoinColumn(name = "contact")
    )
    private List<User> contacts = new ArrayList<>();

    // TODO faire une table ROLES???

//    @Override
//    public String toString() {
//        return "User{" +
//                "id=" + id +
//                ", prenom='" + prenom + '\'' +
//                ", email='" + email + '\'' +
//                ", compte PMB nº" + comptePMB.getId() +
//                ", compte bancaire: IBAN = " + (compte_bancaire == null ? "compte bancaire non défini" : compte_bancaire.getIban()) +
//                ", contacts=" + contacts +
//                '}';
//    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User)) return false;
        // TODO : remplacer prenom par email par la suite
        return this.prenom.equals(((User) obj).prenom);
    }
}
