package fr.renovial.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "devis")
public class Devis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_devis")
    private Integer idDevis;

    @Column(nullable = false, unique = true, length = 20)
    private String reference;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation;

    @Column(name = "taux_tva", nullable = false, precision = 4, scale = 2)
    private BigDecimal tauxTva;

    @Column(name = "total_ht", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalHt;

    @Column(name = "total_ttc", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalTtc;

    @Column(columnDefinition = "TEXT")
    private String precisions;

    @Column(name = "commentaire_admin", columnDefinition = "TEXT")
    private String commentaireAdmin;

    @Column(name = "date_traitement")
    private LocalDateTime dateTraitement;

    @Column(name = "nom_contact", nullable = false, length = 100)
    private String nomContact;

    @Column(name = "email_contact", nullable = false, length = 255)
    private String emailContact;

    @Column(name = "telephone_contact", length = 20)
    private String telephoneContact;

    @ManyToOne
    @JoinColumn(name = "id_utilisateur")
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "id_type_bien", nullable = false)
    private TypeBien typeBien;

    @ManyToOne
    @JoinColumn(name = "id_statut", nullable = false)
    private StatutDevis statutDevis;
}