package aipublish.domain;

import aipublish.SubscriptionandpointApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Point_table")
@Data
//<<< DDD / Aggregate Root
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pointId;

    private Long userId;

    private Integer balance;

    @Embedded
    private UserId userId;

    public static PointRepository repository() {
        PointRepository pointRepository = SubscriptionandpointApplication.applicationContext.getBean(
            PointRepository.class
        );
        return pointRepository;
    }

    //<<< Clean Arch / Port Method
    public void deductPoint(DeductPointCommand deductPointCommand) {
        //implement business logic here:

        PointDeducted pointDeducted = new PointDeducted(this);
        pointDeducted.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method

    //<<< Clean Arch / Port Method
    public static void updatePurchaseList(PointDeducted pointDeducted) {
        //implement business logic here:

        /** Example 1:  new item 
        Point point = new Point();
        repository().save(point);

        */

        /** Example 2:  finding and process
        
        // if pointDeducted.userId exists, use it
        
        // ObjectMapper mapper = new ObjectMapper();
        // Map<Long, Object> pointMap = mapper.convertValue(pointDeducted.getUserId(), Map.class);

        repository().findById(pointDeducted.get???()).ifPresent(point->{
            
            point // do something
            repository().save(point);


         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
