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
}
//>>> Clean Arch / Inbound Adaptor
