package fr.renovial.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "unite")
public class Unite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_unite")
    private Integer idUnite;

    @Column(nullable = false, unique = true, length = 10)
    private String code;

    @Column(nullable = false, length = 30)
    private String libelle;
}