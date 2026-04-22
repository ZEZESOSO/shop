package com.example.shop.controller.product.dto;

import com.example.shop.domain.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductMainDto {
    private Long id;
    private String pName;
    private Integer price;
    private String sellStatus;
    private String imgUrl;
    private boolean isLiked; // 하트 채움 여부

    public static ProductMainDto fromEntity(Product product, boolean isLiked) {
        ProductMainDto dto = new ProductMainDto();
        dto.setId(product.getId());
        dto.setPName(product.getPName());
        dto.setPrice(product.getPrice());
        dto.setSellStatus(String.valueOf(product.getSellStatus()));
        dto.setLiked(isLiked);

        if (product.getProductImgFiles() != null && !product.getProductImgFiles().isEmpty()) {
            dto.setImgUrl(product.getProductImgFiles().get(0).getImgUrl());
        } else {
            dto.setImgUrl("/images/no-image.jpg"); // 기본 이미지 경로
        }
        return dto;
    }
}