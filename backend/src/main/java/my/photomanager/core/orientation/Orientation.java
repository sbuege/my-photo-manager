package my.photomanager.core.orientation;

import jakarta.persistence.*;
import lombok.*;
import my.photomanager.core.tag.TagPrefix;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@ToString
@Getter
public class Orientation {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "external_id", nullable = false, unique = true, updatable = false, length = 75)
    private String externalId;

    @Column(name = "name", nullable = false, unique = true)
    @NonNull
    private String name;

    @PrePersist
    void prePersist() {
        if (externalId == null || externalId.isBlank()) {
            externalId = TagPrefix.ORIENTATION_PREFIX + TagPrefix.PREFIX_SEPARATOR + UUID.randomUUID();
        }
    }
}
