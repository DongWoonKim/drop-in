package com.example.spring.dropin.core.record.controller;

import com.example.spring.dropin.core.record.dto.RecordFindReqeustDTO;
import com.example.spring.dropin.core.record.dto.RecordFindResponseDTO;
import com.example.spring.dropin.core.record.dto.RecordSaveRequestDTO;
import com.example.spring.dropin.core.record.dto.RecordSaveResponseDTO;
import com.example.spring.dropin.core.record.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/records")
public class RecordApiController {

    private final RecordService recordService;

    @GetMapping
    public RecordFindResponseDTO getRecord(@ModelAttribute RecordFindReqeustDTO recordFindReqeustDTO) {
        return recordService.getRecord(recordFindReqeustDTO)
                .orElse(RecordFindResponseDTO.EMPTY);
    }

    @PostMapping
    public RecordSaveResponseDTO save(@RequestBody RecordSaveRequestDTO recordSaveRequestDTO) {
        return recordService.recordSave(recordSaveRequestDTO);
    }

}
