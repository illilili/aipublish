package aipublish.domain;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class PointId implements Serializable {

    private Long id;

    public PointId() {
    }

    public PointId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
