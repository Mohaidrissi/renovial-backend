package fr.renovial.backend.service;

import fr.renovial.backend.dto.response.TypeBienResponse;

import java.util.List;

public interface ITypeBienService {
    List<TypeBienResponse> getTypesBien();
}