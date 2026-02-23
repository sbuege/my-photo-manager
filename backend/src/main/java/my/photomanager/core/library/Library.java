package my.photomanager.core.library;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@ToString
@Getter
public class Library {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "external_id", nullable = false, unique = true, updatable = false, length = 36)
    private String externalId;

    @Column(name = "name", nullable = false, unique = true)
    @NonNull
    @Setter
    private String name;

    @Column(name = "path", nullable = false, unique = true, updatable = false)
    @NonNull
    private String path;

    @Setter
    private Instant lastIndexAt;

    @Setter
    private int numberOfPhotos;

    @PrePersist
    void prePersist() {
        if (externalId == null || externalId.isBlank()) {
            externalId = UUID.randomUUID().toString();
        }
    }
}
