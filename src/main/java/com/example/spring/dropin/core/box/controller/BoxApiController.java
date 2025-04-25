package com.example.spring.dropin.core.box.controller;

import com.example.spring.dropin.core.box.dto.BoxesResponseDTO;
import com.example.spring.dropin.core.box.service.BoxService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boxes")
public class BoxApiController {

    private final BoxService boxService;

    @GetMapping
    public List<BoxesResponseDTO> findAll() {
        return boxService.findAll();
    }

}
