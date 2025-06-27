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
@Table(name = "User_table")
@Data
//<<< DDD / Aggregate Root
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    private String name;

    private String email;

    private String passwordHash;

    private Boolean isKtCustomer;

    private Boolean subscription;

    @Embedded
    private PointId pointId;

    public static UserRepository repository() {
        UserRepository userRepository = SubscriptionandpointApplication.applicationContext.getBean(
            UserRepository.class
        );
        return userRepository;
    }

    //<<< Clean Arch / Port Method
    public void registerUser(RegisterUserCommand registerUserCommand) {
        //implement business logic here:

        UserRegistered userRegistered = new UserRegistered(this);
        userRegistered.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public void grantWelcomePoint(
        GrantWelcomePointCommand grantWelcomePointCommand
    ) {
        //implement business logic here:

        WelcomePointGranted welcomePointGranted = new WelcomePointGranted(this);
        welcomePointGranted.publishAfterCommit();
    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
