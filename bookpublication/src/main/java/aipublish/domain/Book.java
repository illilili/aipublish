package aipublish.domain;

import aipublish.BookpublicationApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Book_table")
@Data
//<<< DDD / Aggregate Root
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long bookId;

    private Long userId;

    private String title;

    private String content;

    private String summary;

    private String coverImageUrl;

    private String category;

    private Integer price;

    private String status;

    private Integer viewCount;

    private Date createdAt;

    // @Embedded
    // private AiBookProcessorId aiBookProcessorId;

    public static BookRepository repository() {
        BookRepository bookRepository = BookpublicationApplication.applicationContext.getBean(
            BookRepository.class
        );
        return bookRepository;
    }

    public void SubmitManuscript() {
        //
    }

    //<<< Clean Arch / Port Method
    public void submitBookCommand(
        SubmitBookCommand submitBookCommandCommand
    ) {
        this.status = "SUBMITTED"; // 상태 변경
        BookSubmittedEvent event = new BookSubmittedEvent(this);
        event.publishAfterCommit(); // 출간 요청 이벤트 발행
    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public void saveBookCommand(SaveBookCommand saveBookCommand) {
        this.userId = saveBookCommand.getUserId();
        this.title = saveBookCommand.getTitle();
        this.content = saveBookCommand.getContent();
        this.status = "DRAFT";
        this.viewCount = 0;
        this.createdAt = new Date();

        SavedBookCommand event = new SavedBookCommand(this);
        event.publishAfterCommit();
        }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
