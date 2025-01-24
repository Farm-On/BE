package com.backend.farmon.domain;

import com.backend.farmon.domain.Board;
import com.backend.farmon.domain.Comment;
import com.backend.farmon.domain.LikeCount;
import com.backend.farmon.domain.User;
import com.backend.farmon.domain.commons.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Table(name="Post_Table")
@Entity
@Getter
@Setter
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_id")
    private Long id;

    private String postTitle;

    private String postContent;

    private int postLike;

    @ManyToOne
    @JoinColumn(name="board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LikeCount> postlikes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
    private List<PostImg> postImgs = new ArrayList<>();



//
//    public void addLike(User user) {
//        LikeCount likeCount = new LikeCount(this, user);
//        this.postlikes.add(likeCount);
//    }
//
//    public void removeLike(User user) {
//        this.postlikes.removeIf(like -> like.getUser().equals(user));
//    }
//
//    public int getLikeCount() {
//        return this.postlikes.size();
//    }


}
