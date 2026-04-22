package com.example.shop.controller.product.dto;

import com.example.shop.domain.Product;
import com.example.shop.domain.SellStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
public class ProductResponse {
    private Long id;
    private String pName;
    private int price;
    private String pDesc; // HTML (텍스트 + 이미지 태그)가 그대로 나감
    private String category;
    private int stock;
    private SellStatus sellStatus;
    private List<ProductImageResponse> productImgFiles; // 슬라이드용 이미지 리스트

    // 엔티티 -> DTO 변환
    public static ProductResponse of(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .pName(product.getPName())
                .price(product.getPrice())
                .pDesc(product.getPDesc())
                .category(product.getCategory())
                .stock(product.getStock())
                .sellStatus(product.getSellStatus())
                .productImgFiles(product.getProductImgFiles().stream()
                        .map(ProductImageResponse::new)
                        .collect(Collectors.toList()))
                .build();
    }

    @Getter
    public static class ProductImageResponse {
        private Long id;          // 기존 이미지의 경우 ID (신규면 null)
        private String imgUrl;
        private String isMain;
        private int sortOrder;

        public ProductImageResponse(com.example.shop.domain.ProductImage image) {
            this.imgUrl = image.getImgUrl();
            this.isMain = image.getIsMain();
            this.sortOrder = image.getSortOrder();
        }
    }
}