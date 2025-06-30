// AdminController.java

package aipublish.web;

import aipublish.domain.UpdateWriterStatusCommandCommand;
import aipublish.domain.Write;
import aipublish.domain.WriteRepository;
import aipublish.domain.WriterStatusChangedEvent; // WriterStatusChangedEvent 임포트 추가
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher; // ApplicationEventPublisher 임포트 추가
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional; // Transactional 임포트 추가
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private WriteRepository writeRepository;

    // Spring의 표준 이벤트 발행 도구를 주입받습니다.
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @PatchMapping("/writers/{userId}/status")
    @Transactional // DB 저장과 이벤트 발행을 하나의 트랜잭션으로 묶어 안정성 확보
    public ResponseEntity<Write> updateWriterStatus(
        @PathVariable Long userId,
        @RequestBody UpdateWriterStatusCommandCommand command
    ) {
        // 1. userId로 작가 지원자 정보를 찾습니다.
        return writeRepository
            .findById(userId)
            .map(write -> {
                // 2. 엔티티의 상태를 변경합니다. (이제 이 메소드는 상태만 바꿉니다)
                write.updateWriterStatusCommand(command);

                // 3. 변경된 정보를 데이터베이스에 저장합니다.
                Write updatedWrite = writeRepository.save(write);

                // 4. DB 저장이 성공한 후, 컨트롤러가 직접 이벤트를 발행합니다.
                applicationEventPublisher.publishEvent(
                    new WriterStatusChangedEvent(updatedWrite)
                );

                return ResponseEntity.ok(updatedWrite);
            })
            .orElseGet(() -> ResponseEntity.notFound().build()); // 작가가 없으면 404 응답
    }
}
