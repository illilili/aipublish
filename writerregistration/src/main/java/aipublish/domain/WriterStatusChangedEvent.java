package aipublish.domain;

import aipublish.domain.*;
import aipublish.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class WriterStatusChangedEvent extends AbstractEvent {

    private Long userId;
    private String name;
    private String email;
    private String bio;
    private String status;

    public WriterStatusChangedEvent(Write aggregate) {
        super(aggregate);
    }

    public WriterStatusChangedEvent() {
        super();
    }
}
//>>> DDD / Domain Event
