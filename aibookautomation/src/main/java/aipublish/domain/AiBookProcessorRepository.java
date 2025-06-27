package aipublish.domain;

import aipublish.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//<<< PoEAA / Repository
@RepositoryRestResource(
    collectionResourceRel = "aiBookProcessors",
    path = "aiBookProcessors"
)
public interface AiBookProcessorRepository
    extends PagingAndSortingRepository<AiBookProcessor, Long> {}
