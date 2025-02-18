package com.backend.farmon.domain;

import com.backend.farmon.domain.commons.BaseEntity;
import com.backend.farmon.dto.post.PostType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Table(name = "post")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Post extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(name = "original_post_id")
    private Long originalPostId; // 원본 게시글 ID

    private String postTitle;

    private String subTitle;

    private String postContent;


    // 게시판과 다대다 관계 (중간 테이블 사용)
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardPost> boardPosts = new ArrayList<>();

    // 편의 메서드
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crop_id")
    private Crop crop;

    public void setBoardPosts(List<BoardPost> boardPosts) {
        this.boardPosts = (boardPosts != null) ? boardPosts : new ArrayList<>();
        for (BoardPost bp : this.boardPosts) {
            bp.setPost(this);
        }
    }

    // 댓글 추가 메서드
    public void addComment(Comment comment) {
        if (comments == null) {
            comments = new ArrayList<>();
        }
        this.comments.add(comment);
        comment.setPost(this);
    }


    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private int postLikes = 0;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LikeCount> postlikes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Answer> answers = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PostImg> postImgs = new ArrayList<>();

    public Post() {
    }

    public void increaseLikes() {
        this.postLikes++;
    }

    public void decreaseLikes() {
        this.postLikes--;
    }

    public int getLikeCount() {
        return this.postlikes.size();
    }
}
