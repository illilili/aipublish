package aipublish.domain;

import aipublish.domain.*;
import aipublish.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class AiPublishingCompleted extends AbstractEvent {

    private Long processorId;
    private Long bookId;
    private Date createdAt;
    private String summary;
    private String coverImageUrl;
    private String category;
    private Integer price;
    private String processStatus;

    public AiPublishingCompleted(AiBookProcessor aggregate) {
        super(aggregate);
    }

    public AiPublishingCompleted() {
        super();
    }
}
//>>> DDD / Domain Event
