package aipublish.infra;

import aipublish.domain.*;
import aipublish.service.AiBookAutomationService;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class AiBookProcessorController {

    private final AiBookProcessorRepository repo;
    private final AiBookAutomationService aiService;

    // 출간 요청 생성 (최초 등록)
    @PostMapping("/aiBookProcessors")
    public ResponseEntity<?> create(@RequestBody AiBookProcessor req) {
        req.setProcessStatus("READY");
        req.setCreatedAt(LocalDateTime.now());
        repo.save(req);
        return ResponseEntity.ok(req);
    }

    // AI 자동화 트리거
    @PostMapping("/aiBookProcessors/{id}/startaipublishing")
    public ResponseEntity<?> startAiPublishing(@PathVariable Long id) {
        AiBookProcessor req = repo.findById(id)
            .orElseThrow(() -> new RuntimeException("출간 요청 없음"));

        req.setProcessStatus("PROCESSING");

        // AI 처리 (summary, 이미지 등 생성)
        AIResult ai = aiService.generate(req.getTitle(), req.getContent());

        req.setSummary(ai.getSummary());
        req.setCoverImageUrl(ai.getCoverImageUrl());
        req.setCategory(ai.getCategory());
        req.setPrice(ai.getPrice());
        req.setProcessStatus("COMPLETE");
        repo.save(req);

        return ResponseEntity.ok(req); // 실제론 바로 업데이트도 가능
    }

    // AI 처리 결과 반영 (메타데이터 업데이트)
    @PostMapping("/aiBookProcessors/{id}/updatebookmetadata")
    public ResponseEntity<?> updateBookMetadata(@PathVariable Long id, @RequestBody UpdateBookMetadataCommand cmd) {
        AiBookProcessor req = repo.findById(id)
            .orElseThrow(() -> new RuntimeException("출간 요청 없음"));

        if (cmd.getBookId() != null) req.setBookId(cmd.getBookId());
        if (cmd.getTitle() != null) req.setTitle(cmd.getTitle());
        if (cmd.getContent() != null) req.setContent(cmd.getContent());
        if (cmd.getSummary() != null) req.setSummary(cmd.getSummary());
        if (cmd.getCoverImageUrl() != null) req.setCoverImageUrl(cmd.getCoverImageUrl());
        if (cmd.getCategory() != null) req.setCategory(cmd.getCategory());
        if (cmd.getPrice() != null) req.setPrice(cmd.getPrice());

        req.setProcessStatus("COMPLETE");
        repo.save(req);

        // (선택) Book 마이크로서비스로 결과 전송
        // bookServiceClient.registerBook(req);

        return ResponseEntity.ok(req);
    }

    // (Optional) 단일 조회
    @GetMapping("/aiBookProcessors/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return repo.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
