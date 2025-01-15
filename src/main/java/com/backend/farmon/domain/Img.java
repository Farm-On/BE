package com.backend.farmon.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
        import lombok.*;

@Table(name = "Img")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Img {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="img_no")
    private Long id; // img_no

    private String storedFileName;

    private String originalFileName;

    // Img:Post = N:1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_no")
    @JsonBackReference
    private Post post;

    public void changePost(Post post){
        if(this.post!=null){
            this.post.getPostImgs().remove(this);
        }
        this.post = post;
//        post.getPostImgs().add(this);
    }

}