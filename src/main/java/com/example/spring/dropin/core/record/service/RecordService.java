package com.example.spring.dropin.core.record.service;

import com.example.spring.dropin.core.record.dto.*;
import com.example.spring.dropin.core.record.repository.RecordRepository;
import com.example.spring.dropin.domain.Record;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;

    public RecordSaveResponseDTO recordSave(RecordSaveRequestDTO recordSaveRequestDTO) {
        Record saved = recordRepository.save(recordSaveRequestDTO.toRecord());
        return RecordSaveResponseDTO.builder()
                .recordId(saved.getId())
                .build();
    }

    public boolean recordUpdate(RecordUpdateRequestDTO recordUpdateRequestDTO) {
        return recordRepository.updateContentByIdAndUserId(
                recordUpdateRequestDTO.getRecordId(),
                recordUpdateRequestDTO.getUserId(),
                recordUpdateRequestDTO.getContent()
        ) > 0;
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

    public Optional<List<RecordAllResponseDTO>> getRecordAll(RecordAllRequestDTO recordAllRequestDTO) {
        try {
            LocalDate date = LocalDate.parse(recordAllRequestDTO.getDate());

            return recordRepository.findByDateAndBox(date, recordAllRequestDTO.getBox())
                    .map(records -> records.stream()
                            .map(record -> RecordAllResponseDTO.builder()
                                    .userId(record.getUserId())
                                    .content(record.getContent()) // 필드명에 맞게 수정
                                    .build())
                            .collect(Collectors.toList())
                    );

        } catch (DateTimeParseException | NullPointerException e) {
            // 날짜 파싱 실패 또는 getDate()가 null인 경우 처리
            log.warn("Invalid date format or null: {}", recordAllRequestDTO.getDate(), e);
            return Optional.empty();
        }
    }
}
