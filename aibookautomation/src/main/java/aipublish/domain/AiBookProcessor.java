package aipublish.domain;

import aipublish.AibookautomationApplication;
import javax.persistence.*;
import java.util.List;
import lombok.Data;
import java.util.Date;
import java.time.LocalDate;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;


@Entity
@Table(name="AiBookProcessor_table")
// @Data

//<<< DDD / Aggregate Root
public class AiBookProcessor  {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    
    
    
private Long processorId;    
    
    
private Long bookId;    
    
    
private String summary;    
    
    
private String coverImageUrl;    
    
    
private String category;    
    
    
private Integer price;    
    
    
private String processStatus;    
    
    
private Date createdAt;    
    
    // @Embedded
// private ManuscriptId manuscriptId;


    public static AiBookProcessorRepository repository(){
        AiBookProcessorRepository aiBookProcessorRepository = AibookautomationApplication.applicationContext.getBean(AiBookProcessorRepository.class);
        return aiBookProcessorRepository;
    }



//<<< Clean Arch / Port Method
    public void startAiPublishing(StartAiPublishingCommand startAiPublishingCommand){
        
        //implement business logic here:
        


        AiPublishingCompleted aiPublishingCompleted = new AiPublishingCompleted(this);
        aiPublishingCompleted.publishAfterCommit();
    }
//>>> Clean Arch / Port Method
//<<< Clean Arch / Port Method
    public void updateBookMetadata(UpdateBookMetadataCommand updateBookMetadataCommand){
        
        //implement business logic here:
        

        // aipublish.external.BookQuery bookQuery = new aipublish.external.BookQuery();
        // // bookQuery.set??()        
        //   = AiBookProcessorApplication.applicationContext
        //     .getBean(aipublish.external.Service.class)
        //     .book(bookQuery);

    }
//>>> Clean Arch / Port Method



}
//>>> DDD / Aggregate Root
