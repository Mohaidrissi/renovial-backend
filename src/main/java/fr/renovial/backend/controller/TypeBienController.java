package fr.renovial.backend.controller;

import fr.renovial.backend.dto.response.TypeBienResponse;
import fr.renovial.backend.service.ITypeBienService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/types-bien")
@RequiredArgsConstructor
public class TypeBienController {

    private final ITypeBienService typeBienService;

    @GetMapping
    public List<TypeBienResponse> getTypesBien() {
        return typeBienService.getTypesBien();
    }
}