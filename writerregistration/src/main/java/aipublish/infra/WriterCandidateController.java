package aipublish.infra;

import aipublish.domain.*;
import aipublish.external.UserClient;
import aipublish.external.UserDto;
import java.util.Optional;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@RestController
@Transactional
public class WriterCandidateController {

    @Autowired
    private WriterCandidateRepository writerCandidateRepository;

    @Autowired
    private UserClient userClient;

    /**
     * 작가 등록 신청 API
     */
    @PostMapping("/writers/apply")
    public WriterCandidate applyWriter(@RequestBody ApplyWriterRegistrationCommand command) {
        // 1. userId 기반으로 회원 정보 조회
        UserDto user = userClient.getUser(command.getUserId());

        // 2. WriterCandidate 생성 및 데이터 설정
        WriterCandidate candidate = new WriterCandidate();
        candidate.apply(command.getBio(), user);

        // 3. 저장 및 이벤트 발행
        writerCandidateRepository.save(candidate);
        candidate.toRequestedEvent().publishAfterCommit();

        return candidate;
    }

    /**
     * 관리자 작가 상태 변경 API
     */
    //[수정] userId -> id  (여러 신청 이력 조회)
    @PatchMapping("/admin/writers/{id}/status")
    public WriterCandidate changeWriterStatus(
        @PathVariable Long id,
        @RequestParam("adminId") Long adminId,  // 요청자 (관리자) ID
        @RequestBody ChangeWriterStatusCommand command
    ) {
        // 관리자 확인
        if (!userClient.isAdmin(adminId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "관리자만 작가 상태를 변경할 수 있습니다.");
        }

        // 작가 상태 변경
        WriterCandidate candidate = writerCandidateRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("지원자를 찾을 수 없습니다."));

        candidate.changeStatus(command.getStatus());
        writerCandidateRepository.save(candidate);
        candidate.toStatusChangedEvent().publishAfterCommit();

        return candidate;
    }

    @GetMapping("/admin/writers")
    // ✅ [수정_0703_4am] 프론트에서 보내는 status 파라미터를 받도록 추가합니다.
    public List<WriterCandidate> getWritersByStatus(
        @RequestParam("adminId") Long adminId,
        @RequestParam("status") String status) {
    
    // 1. 관리자 권한 확인
    if (!userClient.isAdmin(adminId)) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "관리자만 접근할 수 있습니다.");
    }

    // 2. ✅ [수정] 파라미터로 받은 status 값으로 동적으로 조회하도록 변경합니다.
    // "PENDING", "APPROVED" 등의 문자열을 WriterCandidateStatus Enum 타입으로 변환합니다.
    try {
        WriterCandidateStatus statusEnum = WriterCandidateStatus.valueOf(status.toUpperCase());
        return writerCandidateRepository.findByStatus(statusEnum);
    } catch (IllegalArgumentException e) {
        // 잘못된 status 값이 들어올 경우 예외 처리
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효하지 않은 상태 값입니다: " + status);
    }
}
    
    //[수정]
    //Optional → List로 반환되므로,
    //여러 건 중 "하나라도 승인된 게 있으면 true" 반환하도록 수정
    @GetMapping("/writers/{userId}/isApproved")
    public boolean isApprovedWriter(@PathVariable Long userId) {
       return writerCandidateRepository.findByUserId(userId)
        .stream()
        .anyMatch(candidate -> candidate.getStatus() == WriterCandidateStatus.APPROVED);
    }
}
