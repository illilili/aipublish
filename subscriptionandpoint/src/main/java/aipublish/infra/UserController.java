package aipublish.infra;

import aipublish.config.kafka.KafkaProcessor;
import aipublish.domain.*;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder; 
import org.springframework.web.bind.annotation.*;

//<<< Clean Arch / Inbound Adaptor

@RestController
@Transactional
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    KafkaProcessor kafkaProcessor; // KafkaProcessor 주입

    @RequestMapping(
        value = "/users/registeruser",
        method = RequestMethod.POST,
        produces = "application/json;charset=UTF-8"
    )
    public User registerUser(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody RegisterUserCommand registerUserCommand
    ) throws Exception {
        System.out.println("##### /user/registerUser  called #####");

        User user = new User();
    user.registerUser(registerUserCommand);
    userRepository.save(user); // 여기서 ID가 할당됨

    // 포인트 자동 지급
    int welcomeAmount = user.getIsKtCustomer() ? 5000 : 1000;
    Point point = new Point();
    point.grantWelcomePoint(user.getId(), welcomeAmount);
    pointRepository.save(point);


    // save 후 이벤트 발행
    UserRegistered event = new UserRegistered(user);
    event.setUserId(user.getId());
    event.setName(user.getName());
    event.setEmail(user.getEmail());
    event.setIsKtCustomer(user.getIsKtCustomer());
    event.publishAfterCommit();

    return user;
    }

    @Autowired
    PointRepository pointRepository;

    @RequestMapping(
        value = "/users/grantwelcomepoint",
        method = RequestMethod.POST,
        produces = "application/json;charset=UTF-8"
    )
    public User grantWelcomePoint(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody GrantWelcomePointCommand command
    ) throws Exception {
        System.out.println("##### /user/grantWelcomePoint  called #####");

        Optional<User> userOpt = userRepository.findById(command.getUserId());
        if (userOpt.isEmpty()) throw new Exception("User not found");

        User user = userOpt.get();

        Point point = new Point();
        point.grantWelcomePoint(user.getId(), command.getAmount());
        pointRepository.save(point);  // 이벤트는 Point 내부에서 발행됨

        return user;
    }
}
//>>> Clean Arch / Inbound Adaptor
