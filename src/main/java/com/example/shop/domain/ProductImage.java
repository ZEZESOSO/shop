package com.example.shop.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_image_id")
    private Long id;

    private String imgName;      // 실제 저장된 파일명 (UUID 포함)
    private String oriImgName;   // 원본 파일명
    private String imgUrl;       // 이미지 조회 경로
    private String isMain;     // 대표 이미지 여부 (Y/N)
    private int sortOrder;       // 이미지 노출 순서

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    //빌더는 생성자 하나에만 적용하는 것이 깔끔하다.
    @Builder
    public ProductImage(String imgName, String oriImgName, String imgUrl, String isMain, int sortOrder, Product product) {
        this.imgName = imgName;
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl;
        this.isMain = isMain;
        this.sortOrder = sortOrder;
        this.product = product;
    }

    // 대표 이미지 설정 메서드
    public void updateIsMain(String isMain) {
        this.isMain = isMain;
    }

    public void updateImageMetadata(int sortOrder, String isMain) {
        this.sortOrder = sortOrder;
        this.isMain = isMain;
    }
}