package com.example.spring.dropin.core.record.controller;

import com.example.spring.dropin.core.record.dto.*;
import com.example.spring.dropin.core.record.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/records")
public class RecordApiController {

    private final RecordService recordService;

    @GetMapping("/me")
    public RecordFindResponseDTO getRecord(@ModelAttribute RecordFindReqeustDTO recordFindReqeustDTO) {
        return recordService.getRecord(recordFindReqeustDTO)
                .orElse(RecordFindResponseDTO.EMPTY);
    }

    @GetMapping
    public List<RecordAllResponseDTO> getAllRecord(@ModelAttribute RecordAllRequestDTO recordAllRequestDTO) {
        return recordService.getRecordAll(recordAllRequestDTO)
                .orElse(Collections.emptyList());
    }

    @PostMapping
    public RecordSaveResponseDTO save(@RequestBody RecordSaveRequestDTO recordSaveRequestDTO) {
        return recordService.recordSave(recordSaveRequestDTO);
    }

}
