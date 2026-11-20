package fr.renovial.backend.controller;

import fr.renovial.backend.model.TypeBien;
import fr.renovial.backend.repository.ITypeBienRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/types-bien")
@RequiredArgsConstructor
public class TypeBienController {

    private final ITypeBienRepository typeBienRepository;

    @GetMapping
    public List<TypeBien> getTypesBien() {
        return typeBienRepository.findAll();
    }
}