package my.photomanager.core.cameraModel;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@ToString
@Getter
public class CameraModel {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "external_id", nullable = false, unique = true, updatable = false, length = 36)
    private String externalId;

    @Column(name = "name", nullable = false, unique = true)
    @NonNull
    @Setter
    private String name;

    @PrePersist
    void prePersist() {
        if (externalId == null || externalId.isBlank()) {
            externalId = UUID.randomUUID().toString();
        }
    }
}
