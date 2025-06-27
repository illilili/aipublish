package aipublish.infra;

import aipublish.domain.*;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//<<< Clean Arch / Inbound Adaptor

@RestController
// @RequestMapping(value="/users")
@Transactional
public class UserController {

    @Autowired
    UserRepository userRepository;

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
        userRepository.save(user);
        return user;
    }

    @RequestMapping(
        value = "/users/grantwelcomepoint",
        method = RequestMethod.POST,
        produces = "application/json;charset=UTF-8"
    )
    public User grantWelcomePoint(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody GrantWelcomePointCommand grantWelcomePointCommand
    ) throws Exception {
        System.out.println("##### /user/grantWelcomePoint  called #####");
        User user = new User();
        user.grantWelcomePoint(grantWelcomePointCommand);
        userRepository.save(user);
        return user;
    }
}
//>>> Clean Arch / Inbound Adaptor
