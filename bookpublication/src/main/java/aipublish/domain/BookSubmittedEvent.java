package aipublish.domain;

import aipublish.domain.*;
import aipublish.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class BookSubmittedEvent extends AbstractEvent {

    private Long bookId;
    private Long userId;
    private String title;
    private String content;
    private String status;

    public BookSubmittedEvent(Book aggregate) {
        super(aggregate);
    }

    public BookSubmittedEvent() {
        super();
    }
}
//>>> DDD / Domain Event
