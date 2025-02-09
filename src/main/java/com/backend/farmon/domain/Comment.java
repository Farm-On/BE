package com.backend.farmon.domain;

import com.backend.farmon.domain.Post;
import com.backend.farmon.domain.User;
import com.backend.farmon.domain.commons.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Table(name = "comment")
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Setter
    @NotNull
    @Column(name = "comment_content", nullable = false)
    private String content;

    @NotNull
    @Column(nullable = false)
    private String authorName;

    @ManyToOne(fetch = FetchType.LAZY,optional = true)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    @Setter
    @Column(name = "group_id", nullable = true)
    private Long groupId;

    @Column(name = "group_order", nullable = true)
    private Integer groupOrder;

    @Column(nullable = false)
    private Integer depth;


    @Setter
    @Column(nullable = false)
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;




}
