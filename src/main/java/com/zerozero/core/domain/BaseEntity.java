package com.zerozero.core.domain;

import com.fasterxml.uuid.Generators;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@MappedSuperclass
@EntityListeners(value = AuditingEntityListener.class)
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class BaseEntity {

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Builder.Default
    private Boolean deleted = false;

    @PrePersist
    public void initSequentialUUID() {
        final String UUID_DELIMITER = "-";
        UUID uuid = Generators.timeBasedGenerator().generate();
        String[] uuidSplitArray = uuid.toString().split(UUID_DELIMITER);
        String sequentialUUIDString = uuidSplitArray[2] + uuidSplitArray[1] + uuidSplitArray[0] + uuidSplitArray[3] + uuidSplitArray[4];
        this.id = UUID.fromString(
                new StringBuilder(sequentialUUIDString)
                        .insert(8, UUID_DELIMITER)
                        .insert(13, UUID_DELIMITER)
                        .insert(18, UUID_DELIMITER)
                        .insert(23, UUID_DELIMITER)
                        .toString()
        );
    }
}
