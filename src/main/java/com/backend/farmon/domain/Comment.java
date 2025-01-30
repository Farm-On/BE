package com.backend.farmon.domain;

import com.backend.farmon.domain.commons.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Table(name="comment")
@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
public class Comment extends BaseEntity {
    // 수정까지 가능


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="comment_id")
    private Long id;
    //id로 통일

    private String commentContent;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="PARENT_ID")
    private Comment parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    //삭제기능은 구현 안함

    //일단 사용자 : 댓글 1:ㅜ
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;


    //부모댓글과 대댓글
    @OneToMany(mappedBy="parent",fetch=FetchType.LAZY,
    cascade = {CascadeType.ALL},orphanRemoval = true)
    private List<Comment> children=new ArrayList<>();


    public Comment() {
        
    }
}
