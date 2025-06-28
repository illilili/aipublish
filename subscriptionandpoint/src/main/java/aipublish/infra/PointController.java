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
// @RequestMapping(value="/points")
@Transactional
public class PointController {

    @Autowired
    PointRepository pointRepository;
    @Autowired
    UserRepository userRepository;

    @RequestMapping(
        value = "/points/deductpoint",
        method = RequestMethod.POST,
        produces = "application/json;charset=UTF-8"
    )
    public Point deductPoint(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody DeductPointCommand deductPointCommand
    ) throws Exception {
        System.out.println("##### /point/deductPoint  called #####");
        Point point = new Point();
        point.deductPoint(deductPointCommand);
        pointRepository.save(point);
        return point;
    }

    //포인트조회
    @GetMapping(value = "/users/{id}/points", produces = "application/json;charset=UTF-8")
    public Point getUserPoints(@PathVariable("id") Long id) throws Exception {
        System.out.println("##### /users/{id}/points called, id: " + id);

            Optional<User> userOpt = userRepository.findById(id);
            if (userOpt.isEmpty()) {
                throw new Exception("User not found with id: " + id);
            }

            // 유저가 있으면 해당 유저의 포인트 조회
            User user = userOpt.get();

            // Id로 Point 조회
            Optional<Point> pointOpt = pointRepository.findByUserId(user.getId());

            if (pointOpt.isEmpty()) {
                throw new Exception("Point not found for user id: " + id);
            }

            return pointOpt.get();
    }
    
}
//>>> Clean Arch / Inbound Adaptor
