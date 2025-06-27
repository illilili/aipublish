package aipublish.domain;

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

//<<< DDD / Value Object
@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article {

    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long articleId;

    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long writerId;

    @GeneratedValue(strategy = GenerationType.AUTO)
    private String title;

    @GeneratedValue(strategy = GenerationType.AUTO)
    private String content;

    @GeneratedValue(strategy = GenerationType.AUTO)
    private String summary;

    @GeneratedValue(strategy = GenerationType.AUTO)
    private String coverImageUrl;

    @GeneratedValue(strategy = GenerationType.AUTO)
    private String category;

    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer price;

    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer viewCount;

    @GeneratedValue(strategy = GenerationType.AUTO)
    private Date createdAt;

    @GeneratedValue(strategy = GenerationType.AUTO)
    private Date updatedAt;
}
//>>> DDD / Value Object
