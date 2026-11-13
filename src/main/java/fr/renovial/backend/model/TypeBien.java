package fr.renovial.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "type_bien")
public class TypeBien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_bien")
    private Integer idTypeBien;

    @Column(nullable = false, unique = true, length = 30)
    private String libelle;
}