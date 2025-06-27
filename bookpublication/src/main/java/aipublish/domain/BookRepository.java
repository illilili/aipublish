package aipublish.domain;

import aipublish.domain.*;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//<<< PoEAA / Repository
@RepositoryRestResource(collectionResourceRel = "books", path = "books")
public interface BookRepository extends PagingAndSortingRepository<Book, Long> {
    @Query(
        value = "select book " +
        "from Book book " +
        "where(:manuscriptId is null or book.manuscriptId = :manuscriptId) and (:title is null or book.title like %:title%) and (:userId is null or book.userId = :userId) and (:content is null or book.content like %:content%) and (:summary is null or book.summary like %:summary%) and (:coverImageUrl is null or book.coverImageUrl like %:coverImageUrl%) and (:category is null or book.category like %:category%) and (:price is null or book.price = :price) and (:status is null or book.status like %:status%) and (:viewCount is null or book.viewCount = :viewCount) and (:createdAt is null or book.createdAt = :createdAt)"
    )
    Book bookDetails(
        Long manuscriptId,
        String title,
        Long userId,
        String content,
        String summary,
        String coverImageUrl,
        String category,
        Integer price,
        String status,
        Integer viewCount,
        Date createdAt
    );
}
