package com.backend.farmon.domain;

import com.backend.farmon.domain.Board;
import com.backend.farmon.domain.Comment;
import com.backend.farmon.domain.LikeCount;
import com.backend.farmon.domain.User;
import com.backend.farmon.domain.commons.BaseEntity;
import com.backend.farmon.dto.Filter.FieldCategory;
import com.backend.farmon.dto.post.PostType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Table(name="Post_Table")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_id")
    private Long id;

    private String postTitle;

    private String postContent;

    private PostType postType;

    @ManyToOne
    @JoinColumn(name="board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    @JsonBackReference
    private User user;

    private int poslikes;

    @Enumerated(EnumType.STRING)
    private FieldCategory fieldCategory;

    // 좋아요 순(Qureydsl로 가져오는걸로 가능)
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LikeCount> postlikes = new ArrayList<>();

    //댓글 갯수 저장
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    // 답변 가져오기
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
    private List<PostImg> postImgs = new ArrayList<>();

    public Post() {

    }

    public void increaseLikes() {
        this.poslikes++;
    }

    public void decreaseLikes() {
        this.poslikes--;
    }

    public int getLikeCount() {
        return this.postlikes.size();
    }

}
