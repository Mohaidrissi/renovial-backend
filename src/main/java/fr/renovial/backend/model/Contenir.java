package fr.renovial.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "contenir")
public class Contenir {

    @EmbeddedId
    private ContenirId id;

    @ManyToOne
    @MapsId("idService")
    @JoinColumn(name = "id_service")
    private Service service;

    @ManyToOne
    @MapsId("idDevis")
    @JoinColumn(name = "id_devis")
    private Devis devis;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal quantite;

    @Column(name = "prix_unitaire", nullable = false, precision = 10, scale = 2)
    private BigDecimal prixUnitaire;
}