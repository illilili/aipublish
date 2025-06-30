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
        value = "/points/deduct",
        method = RequestMethod.POST,
        produces = "application/json;charset=UTF-8"
    )
    public Point deductPoint(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody DeductPointCommand deductPointCommand
    ) throws Exception {
        System.out.println("##### /point/deductPoint  called #####");
        Optional<Point> pointOpt = pointRepository.findByUserId(deductPointCommand.getUserId());

        if (pointOpt.isEmpty()) {
            throw new RuntimeException("포인트 정보가 존재하지 않습니다.");
        }

        Point point = pointOpt.get();
        point.deductPoint(deductPointCommand);
        pointRepository.save(point);
        return point;
    }

    //포인트조회
    @GetMapping(value = "/points/balance", produces = "application/json;charset=UTF-8")
    public Point getUserPointBalance(@RequestParam("userId") Long userId) throws Exception {
        System.out.println("##### /points/balance called, userId: " + userId);

        // 유저 존재 여부 확인
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new Exception("User not found with id: " + userId);
        }

        // 포인트 정보 조회
        Optional<Point> pointOpt = pointRepository.findByUserId(userId);
        if (pointOpt.isEmpty()) {
            throw new Exception("Point not found for user id: " + userId);
        }

        return pointOpt.get();
    }

    @PostMapping(value = "/points/grant", produces = "application/json;charset=UTF-8")
    public Point grantPoint(@RequestBody GrantPointCommand command) throws Exception {
        System.out.println("##### /points/grant called #####");

        Optional<Point> pointOpt = pointRepository.findByUserId(command.getUserId());

        Point point;

        if (pointOpt.isPresent()) {
            // 기존 포인트에 추가
            point = pointOpt.get();
            point.setBalance(point.getBalance() + command.getAmount());
        } else {
            // 없으면 새로 생성
            point = new Point();
            point.setUserId(command.getUserId());
            point.setBalance(command.getAmount());
        }

        pointRepository.save(point);

        return point;
    }
    
}
//>>> Clean Arch / Inbound Adaptor