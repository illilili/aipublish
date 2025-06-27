package aipublish.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class SaveBookCommandCommand {

    private Long bookId;
    private Long userId;
    private String title;
    private String content;
    private String status;
    private Long manuscriptId;
    private Long writerId;
    private String title;
    private String content;
    private String status;
}
