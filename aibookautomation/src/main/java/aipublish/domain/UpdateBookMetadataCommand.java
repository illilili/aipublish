package aipublish.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class UpdateBookMetadataCommand {

    private String summary;
    private String coverImageUrl;
    private String category;
    private Integer price;
    private String bookId;
    private String title;
    private String content;
}
