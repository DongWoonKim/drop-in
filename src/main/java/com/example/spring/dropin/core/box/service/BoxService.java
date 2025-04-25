package com.example.spring.dropin.core.box.service;

import com.example.spring.dropin.core.box.dto.BoxesResponseDTO;
import com.example.spring.dropin.core.box.repository.BoxRepository;
import com.example.spring.dropin.domain.Box;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class BoxService {

    private final BoxRepository boxRepository;

    public List<BoxesResponseDTO> findAll() {
        return boxRepository.findAll().stream()
                .map(Box::toBoxesResponseDTO)
                .collect(toList());
    }

}
