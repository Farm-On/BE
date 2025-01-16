package com.backend.farmon.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Table(name="Board")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {
    //게시판 이름
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Enumerated(value=EnumType.STRING)
//    private BoardName boardName; 아직 정해지지 않음

    @OneToMany(mappedBy = "board",cascade = CascadeType.ALL)
    private List<Post> boardPosts=new ArrayList<>();



}
