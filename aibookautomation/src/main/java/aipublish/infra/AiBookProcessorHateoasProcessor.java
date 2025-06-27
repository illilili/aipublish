package aipublish.infra;

import aipublish.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class AiBookProcessorHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<AiBookProcessor>> {

    @Override
    public EntityModel<AiBookProcessor> process(
        EntityModel<AiBookProcessor> model
    ) {
        model.add(
            Link
                .of(
                    model.getRequiredLink("self").getHref() +
                    "/startaipublishing"
                )
                .withRel("startaipublishing")
        );
        model.add(
            Link
                .of(
                    model.getRequiredLink("self").getHref() +
                    "/updatebookmetadata"
                )
                .withRel("updatebookmetadata")
        );

        return model;
    }
}
