package fr.renovial.backend.service.impl;

import fr.renovial.backend.dto.response.TypeBienResponse;
import fr.renovial.backend.repository.ITypeBienRepository;
import fr.renovial.backend.service.ITypeBienService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TypeBienService implements ITypeBienService {

    private final ITypeBienRepository typeBienRepository;

    @Override
    public List<TypeBienResponse> getTypesBien() {
        return typeBienRepository.findAll().stream()
                .map(type -> new TypeBienResponse(type.getIdTypeBien(), type.getLibelle(), type.getProfessionnel()))
                .toList();
    }
}