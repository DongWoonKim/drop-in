package com.example.spring.dropin.core.wod.controller;

import com.example.spring.dropin.core.wod.dto.WodRequestDTO;
import com.example.spring.dropin.core.wod.dto.WodResponseDTO;
import com.example.spring.dropin.core.wod.service.WodService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wods")
public class WodApiController {
    private final WodService wodService;

    @GetMapping
    public WodResponseDTO getWod(@ModelAttribute WodRequestDTO wodRequestDTO) {
        return wodService.getByDateAndBox(wodRequestDTO)
                .orElse(WodResponseDTO.EMPTY);
    }

}
