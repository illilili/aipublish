package aipublish.domain;

import aipublish.WriterregistrationApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Write_table")
@Data
//<<< DDD / Aggregate Root
public class Write {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    private String name;

    private String email;

    private String bio;

    private String status;

    private Date createdAt;

    @Embedded
    private ManuscriptId manuscriptId;

    public static WriteRepository repository() {
        WriteRepository writeRepository = WriterregistrationApplication.applicationContext.getBean(
            WriteRepository.class
        );
        return writeRepository;
    }

    public void ApplyWriterRegistration() {
        //
    }

    //<<< Clean Arch / Port Method
    public void registerWriterCommand(
        RegisterWriterCommandCommand registerWriterCommandCommand
    ) {
        //implement business logic here:

        WriterStatusChangedEvent writerStatusChangedEvent = new WriterStatusChangedEvent(
            this
        );
        writerStatusChangedEvent.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public void updateWriterStatusCommand(
        UpdateWriterStatusCommandCommand updateWriterStatusCommandCommand
    ) {
        //implement business logic here:

        WriterStatusChangedEvent writerStatusChangedEvent = new WriterStatusChangedEvent(
            this
        );
        writerStatusChangedEvent.publishAfterCommit();
    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
