package com.example.spring.dropin.core.wod.service;

import com.example.spring.dropin.core.wod.dto.WodRequestDTO;
import com.example.spring.dropin.core.wod.dto.WodResponseDTO;
import com.example.spring.dropin.core.wod.repository.WodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WodService {
    private final WodRepository wodRepository;

    public Optional<WodResponseDTO> getByDateAndBox(WodRequestDTO dto) {
        LocalDate date = LocalDate.parse(dto.getDate());
        String box     = dto.getBox();

        return wodRepository
                .findOneByCreatedDateAndBox(date, box)
                .map(wod -> WodResponseDTO.builder()
                        .title(wod.getTitle())
                        .program(wod.getProgram())
                        .build()
                );
    }

}
