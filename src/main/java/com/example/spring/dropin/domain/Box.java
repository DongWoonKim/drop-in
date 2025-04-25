package com.example.spring.dropin.domain;


import com.example.spring.dropin.core.box.dto.BoxesResponseDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "box")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Box {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "box_name", nullable = false, length = 100)
    private String boxName;

    @Column(name = "owner_id", length = 30)
    private String ownerId;

    @Column(name = "created", nullable = false, updatable = false)
    private LocalDateTime created;

    @PrePersist
    protected void onCreate() {
        this.created = LocalDateTime.now();
    }

    public BoxesResponseDTO toBoxesResponseDTO() {
        return BoxesResponseDTO.builder()
                .id(this.id)
                .boxName(this.boxName)
                .build();
    }
}
