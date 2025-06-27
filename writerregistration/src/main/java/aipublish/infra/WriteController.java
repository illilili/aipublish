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
// @RequestMapping(value="/writes")
@Transactional
public class WriteController {

    @Autowired
    WriteRepository writeRepository;

    @RequestMapping(
        value = "/writes/registerwritercommand",
        method = RequestMethod.POST,
        produces = "application/json;charset=UTF-8"
    )
    public Write registerWriterCommand(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody RegisterWriterCommandCommand registerWriterCommandCommand
    ) throws Exception {
        System.out.println("##### /write/registerWriterCommand  called #####");
        Write write = new Write();
        write.registerWriterCommand(registerWriterCommandCommand);
        writeRepository.save(write);
        return write;
    }

    @RequestMapping(
        value = "/writes/{id}/updatewriterstatuscommand",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public Write updateWriterStatusCommand(
        @PathVariable(value = "id") Long id,
        @RequestBody UpdateWriterStatusCommandCommand updateWriterStatusCommandCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println(
            "##### /write/updateWriterStatusCommand  called #####"
        );
        Optional<Write> optionalWrite = writeRepository.findById(id);

        optionalWrite.orElseThrow(() -> new Exception("No Entity Found"));
        Write write = optionalWrite.get();
        write.updateWriterStatusCommand(updateWriterStatusCommandCommand);

        writeRepository.save(write);
        return write;
    }
}
//>>> Clean Arch / Inbound Adaptor
