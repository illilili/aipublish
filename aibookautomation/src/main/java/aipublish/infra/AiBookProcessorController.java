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
// @RequestMapping(value="/aiBookProcessors")
@Transactional
public class AiBookProcessorController {

    @Autowired
    AiBookProcessorRepository aiBookProcessorRepository;

    @RequestMapping(
        value = "/aiBookProcessors/startaipublishing",
        method = RequestMethod.POST,
        produces = "application/json;charset=UTF-8"
    )
    public AiBookProcessor startAiPublishing(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody StartAiPublishingCommand startAiPublishingCommand
    ) throws Exception {
        System.out.println(
            "##### /aiBookProcessor/startAiPublishing  called #####"
        );
        AiBookProcessor aiBookProcessor = new AiBookProcessor();
        aiBookProcessor.startAiPublishing(startAiPublishingCommand);
        aiBookProcessorRepository.save(aiBookProcessor);
        return aiBookProcessor;
    }

    @RequestMapping(
        value = "/aiBookProcessors/{id}/updatebookmetadata",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public AiBookProcessor updateBookMetadata(
        @PathVariable(value = "id") Long id,
        @RequestBody UpdateBookMetadataCommand updateBookMetadataCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println(
            "##### /aiBookProcessor/updateBookMetadata  called #####"
        );
        Optional<AiBookProcessor> optionalAiBookProcessor = aiBookProcessorRepository.findById(
            id
        );

        optionalAiBookProcessor.orElseThrow(() ->
            new Exception("No Entity Found")
        );
        AiBookProcessor aiBookProcessor = optionalAiBookProcessor.get();
        aiBookProcessor.updateBookMetadata(updateBookMetadataCommand);

        aiBookProcessorRepository.save(aiBookProcessor);
        return aiBookProcessor;
    }
}
//>>> Clean Arch / Inbound Adaptor
