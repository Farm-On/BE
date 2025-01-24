package com.backend.farmon.domain;

import com.backend.farmon.domain.commons.BaseEntity;
import com.backend.farmon.domain.mapping.ExpertArea;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Expert extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String profileImageUrl; // 전문기 프로필 이미지

    private String expertDescription; // 전문가 한줄소개

    private Float rating;

    private String availableRange; //활동 가능 범위

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isAvailableEverywhere; // 전국 어디든 가능 여부

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isExcludeIsland; // 도서 지방 제외 여부

    private String serviceDetail1;
    private String serviceDetail2;
    private String serviceDetail3;
    private String serviceDetail4;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;  // user와 1:1관계

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crop_id")
    private Crop crop; // 전문가와 작물은 1:N관계

    @OneToMany(mappedBy = "expert", cascade = CascadeType.ALL)
    private List<Estimate> estimateList = new ArrayList<>();

    @OneToMany(mappedBy = "expert", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExpertArea> expertAreaList = new ArrayList<>();

    @OneToMany(mappedBy = "expert", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Portfolio> portfolioList = new ArrayList<>();

    @OneToMany(mappedBy = "expert", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExpertDatail> expertDatailList = new ArrayList<>();

    @OneToMany(mappedBy = "expert", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExpertCareer> expertCareerList = new ArrayList<>();

//    @OneToMany(mappedBy = "expert", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Estimate> estimateList = new ArrayList<>();

    @OneToMany(mappedBy = "expert", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoom> chatRoomList = new ArrayList<>();
}