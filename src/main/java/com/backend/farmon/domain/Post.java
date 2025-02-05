package com.backend.farmon.domain;

import com.backend.farmon.domain.commons.BaseEntity;
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

    private String postTitle;

    private String postContent;

//
//    private PostType postType;

    // 상위 분야 이름
    private String Category;

    @CollectionTable(name = "post_sub_categories", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "sub_category")
    private String subCategories;

    // 하위 카테고리
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostCrop> postCrops = new ArrayList<>();

    // 편의 메서드: Crop 추가
    public void addCrop(Crop crop) {
        PostCrop postCrop = new PostCrop();
        postCrop.setPost(this);
        postCrop.setCrop(crop);
        this.postCrops.add(postCrop);
        crop.getPostCrops().add(postCrop);
    }


//    @ElementCollection
//    @CollectionTable(name = "post_subcategories", joinColumns = @JoinColumn(name = "post_id"))
//    @Column(name = "sub_category_name")
//    private List<String> selectedSubCategories; // 선택된 하위 카테고리 리스트

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

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
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