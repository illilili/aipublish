package aipublish.domain;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiBookProcessor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bookId;         // 외부(책)와 매핑용
    private String title;
    private String content;

    // AI 처리 결과
    private String summary;
    private String coverImageUrl;
    private String category;
    private Integer price;

    private String processStatus;   // READY/PROCESSING/COMPLETE/FAILED
    private LocalDateTime createdAt;
}
