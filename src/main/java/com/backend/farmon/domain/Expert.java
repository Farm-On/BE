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
public class Expert extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(nullable = false) 와이어프레임 나오면 결정예정
    private String expertDescription; // 전문가 한줄소개

//    @Column(nullable = false) 와이어프레임 나오면 결정예정
    private Integer career;

//    @Column(nullable = false) 와이어프레임 나오면 결정예정
    private Integer availableRage;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;  // user와 1:1관계

    @OneToMany(mappedBy = "expert", cascade = CascadeType.ALL)
    private List<Estimate> estimateList = new ArrayList<>();

    @OneToMany(mappedBy = "expert", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExpertArea> expertAreaList = new ArrayList<>();

    @OneToMany(mappedBy = "expert", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Portfolio> portfolioList = new ArrayList<>();
}