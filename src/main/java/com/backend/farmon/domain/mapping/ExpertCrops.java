package com.backend.farmon.domain.mapping;

import com.backend.farmon.domain.Crop;
import com.backend.farmon.domain.Expert;
import com.backend.farmon.domain.commons.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ExpertCrops extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expert_id")
    private Expert Expert;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "crop_id")
//    private Crop crop;  // 작물 양방향 매핑
}