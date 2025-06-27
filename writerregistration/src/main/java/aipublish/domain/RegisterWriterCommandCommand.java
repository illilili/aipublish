package aipublish.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class RegisterWriterCommandCommand {

    private String name;
    private String email;
    private String bio;
}
