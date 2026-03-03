package my.photomanager.core.location;

import jakarta.persistence.*;
import lombok.*;
import my.photomanager.core.tag.TagPrefix;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"country", "city"}))
@ToString
@Getter
public class Location {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "external_id", nullable = false, unique = true, updatable = false, length = 75)
    private String externalId;

    @Column(name = "country", nullable = false)
    @NonNull
    @Setter
    private String country;

    @Column(name = "city", nullable = false)
    @NonNull
    @Setter
    private String city;

    @PrePersist
    void prePersist() {
        if (externalId == null || externalId.isBlank()) {
            externalId = TagPrefix.LOCATION_PREFIX + TagPrefix.PREFIX_SEPARATOR + UUID.randomUUID();
        }
    }
}
