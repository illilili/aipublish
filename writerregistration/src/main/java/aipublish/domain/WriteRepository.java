package aipublish.domain;

import aipublish.domain.*;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//<<< PoEAA / Repository
@RepositoryRestResource(collectionResourceRel = "writes", path = "writes")
public interface WriteRepository
    extends PagingAndSortingRepository<Write, Long> {
    @Query(
        value = "select write " +
        "from Write write " +
        "where(:id is null or write.id = :id) and (:name is null or write.name like %:name%) and (:status is null or write.status like %:status%) and (:createdAt is null or write.createdAt = :createdAt) and (:status is null or write.status like %:status%) and (:createdAt is null or write.createdAt = :createdAt)"
    )
    List<Write> writerList(
        Long id,
        String name,
        String status,
        Date createdAt,
        String status,
        Date createdAt,
        Pageable pageable
    );

    @Query(
        value = "select write " +
        "from Write write " +
        "where(:id is null or write.id = :id) and (:name is null or write.name like %:name%) and (:email is null or write.email like %:email%) and (:bio is null or write.bio like %:bio%) and (:status is null or write.status like %:status%) and (:createdAt is null or write.createdAt = :createdAt)"
    )
    Write writerDetails(
        Long id,
        String name,
        String email,
        String bio,
        String status,
        Date createdAt
    );
}
