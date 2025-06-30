package aipublish.domain;

import aipublish.WriterregistrationApplication;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Entity
@Table(name = "Write_table")
@Data
public class Write {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    private String name;
    private String email;
    private String bio;

    @Enumerated(EnumType.STRING)
    private WriterCandidateStatus status;

    private Date createdAt;

    @PrePersist
    public void prePersist() {
        this.status = WriterCandidateStatus.PENDING;
        this.createdAt = new Date();
    }

    public static WriteRepository repository() {
        return WriterregistrationApplication.applicationContext.getBean(
            WriteRepository.class
        );
    }

    public void ApplyWriterRegistration() {
        // 필요한 경우 이 메소드에 추가 로직 구현
    }

    public void registerWriterCommand(
        RegisterWriterCommandCommand registerWriterCommandCommand
    ) {
        // Command의 정보로 엔티티 필드 설정
        this.setName(registerWriterCommandCommand.getName());
        this.setEmail(registerWriterCommandCommand.getEmail());
        this.setBio(registerWriterCommandCommand.getBio());

        // 이벤트 생성 및 발행
        WriterStatusChangedEvent writerStatusChangedEvent = new WriterStatusChangedEvent();
        BeanUtils.copyProperties(this, writerStatusChangedEvent);
        // BeanUtils가 userId를 복사 못하는 경우가 있으므로 명시적으
    }

    public void updateWriterStatusCommand(
        UpdateWriterStatusCommandCommand updateWriterStatusCommandCommand
    ) {
        // Command의 status 문자열을 Enum으로 변환
        WriterCandidateStatus newStatus = WriterCandidateStatus.valueOf(
            updateWriterStatusCommandCommand.getStatus().toUpperCase()
        );
        // 엔티티 상태 업데이트 (이벤트 발행 코드는 완전히 삭제)
        this.setStatus(newStatus);
    }
}
