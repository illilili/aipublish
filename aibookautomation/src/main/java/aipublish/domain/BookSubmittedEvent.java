package aipublish.domain;

import aipublish.domain.*;
import aipublish.infra.AbstractEvent;
import java.util.*;
import lombok.*;

@Data
@ToString
public class BookSubmittedEvent extends AbstractEvent {

    private Long bookId;
    private Long userId;
    private String title;
    private String content;
    private String status;
}
