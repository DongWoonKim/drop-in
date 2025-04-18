package com.example.spring.dropin.core.record.service;

import com.example.spring.dropin.core.record.dto.RecordFindReqeustDTO;
import com.example.spring.dropin.core.record.dto.RecordFindResponseDTO;
import com.example.spring.dropin.core.record.dto.RecordSaveRequestDTO;
import com.example.spring.dropin.core.record.dto.RecordSaveResponseDTO;
import com.example.spring.dropin.core.record.repository.RecordRepository;
import com.example.spring.dropin.core.wod.dto.WodResponseDTO;
import com.example.spring.dropin.domain.Record;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;

    public RecordSaveResponseDTO recordSave(RecordSaveRequestDTO recordSaveRequestDTO) {
        return RecordSaveResponseDTO.builder()
                .content( recordRepository.save(recordSaveRequestDTO.toRecord()).getContent() )
                .build();
    }

    public Optional<RecordFindResponseDTO> getRecord(RecordFindReqeustDTO recordFindReqeustDTO) {
        LocalDate date = LocalDate.parse(recordFindReqeustDTO.getDate());
        return recordRepository.findByUserIdAndDateAndBox(recordFindReqeustDTO.getUserId(), date, recordFindReqeustDTO.getBox())
                .map(record -> RecordFindResponseDTO.builder()
                        .id(record.getId())
                        .content( record.getContent() )
                        .build()
                );
    }

}
