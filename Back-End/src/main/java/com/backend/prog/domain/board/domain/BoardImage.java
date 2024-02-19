package com.backend.prog.domain.board.domain;

import com.backend.prog.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "board_image")
public class BoardImage extends BaseEntity {
    @Id
    @Column(name = "board_img_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("게시글이미지ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Comment("이미지주소")
    private String imgUrl;

    @Builder
    private BoardImage(Board board, String imgUrl) {
        this.board = board;
        this.imgUrl = imgUrl;
    }
}
