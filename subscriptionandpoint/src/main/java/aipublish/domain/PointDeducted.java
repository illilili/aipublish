package aipublish.domain;

import aipublish.domain.*;
import aipublish.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class PointDeducted extends AbstractEvent {

    private Long pointId;
    private Long userId;
    private Integer amount;
    private Long bookId;

    public PointDeducted(Point aggregate) {
        super(aggregate);
    }

    public PointDeducted() {
        super();
    }
}
//>>> DDD / Domain Event
