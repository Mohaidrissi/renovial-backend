package fr.renovial.backend.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class ContenirId implements Serializable {

    private Integer idService;

    private Integer idDevis;
}