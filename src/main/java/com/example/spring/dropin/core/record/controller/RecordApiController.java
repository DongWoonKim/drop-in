package com.example.spring.dropin.core.record.controller;

import com.example.spring.dropin.core.record.dto.*;
import com.example.spring.dropin.core.record.service.RecordService;
import com.example.spring.dropin.util.HtmlSanitizerUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

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

    @PostMapping("/me")
    public RecordSaveResponseDTO save(@RequestBody RecordSaveRequestDTO recordSaveRequestDTO) {
        // 🛡️ 서버에서도 content XSS 방어
        String safeContent = HtmlSanitizerUtil.sanitize(recordSaveRequestDTO.getContent());
        // safeContent를 세팅해서 저장하도록 DTO 수정
        recordSaveRequestDTO.setContent(safeContent);

        return recordService.saveOrUpdateRecord(recordSaveRequestDTO);
    }

    @PutMapping("/me")
    public RecordUpdateResponseDTO update(@RequestBody RecordUpdateRequestDTO recordUpdateRequestDTO) {
        return RecordUpdateResponseDTO.builder()
                .success(recordService.recordUpdate(recordUpdateRequestDTO))
                .build();
    }


}
