package aipublish.domain;

import aipublish.domain.*;
import aipublish.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class WelcomePointGranted extends AbstractEvent {

    private Long userId;
    private Integer amount;
    private Date grantedAt;

    public WelcomePointGranted(User aggregate) {
        super(aggregate);
    }

    public WelcomePointGranted() {
        super();
    }
}
//>>> DDD / Domain Event
