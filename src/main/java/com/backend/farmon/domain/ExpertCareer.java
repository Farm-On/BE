package com.backend.farmon.domain;

import com.backend.farmon.domain.commons.BaseEntity;
import com.backend.farmon.domain.mapping.ExpertArea;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ExpertCareer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer startYear;

    @Column(nullable = false)
    private Integer startMonth;

    @Column(nullable = false)
    private Integer endYear;

    @Column(nullable = false)
    private Integer endMonth;

    private String detailContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expert_id")
    private Expert expert;
}