package aipublish.domain;

import java.util.Date;
import lombok.Data;

@Data
public class WriterDetailsQuery {

    private Long id;
    private String name;
    private String email;
    private String bio;
    private String status;
    private Date createdAt;
}
