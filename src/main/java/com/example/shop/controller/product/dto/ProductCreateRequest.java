package com.example.shop.controller.product.dto;

import com.example.shop.domain.Product;
import com.example.shop.domain.SellStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter //불변
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 파라미터가 없는 기본 생성자 생성 (JSON 역직렬화 시 필요)
@AllArgsConstructor // 모든 필드를 인자로 받는 생성자 생성
public class ProductCreateRequest {

    @NotBlank(message = "상품명은 필수입니다.")
    private String pName;

    @Positive(message = "가격은 0원 초과이어야 합니다.")
    private int price;

    private String pDesc;
    private String category;

    @PositiveOrZero(message = "재고는 0개 이상이어야 합니다.")
    private int stock;

    @Enumerated(EnumType.STRING)
    private SellStatus sellStatus; // 상품 판매 상태 (SELL, SOLD_OUT)


    //이미지 파일들을 받기 위한 필드 추가
    private List<MultipartFile> productImgFiles; //MultipartFile 객체 자체에 파일의 원본을 가져오는 메서드가 내장되어 있음

    // DTO(요청 데이터)를 Product 엔티티로 변환하는 메서드
    public Product toEntity(){
        return Product.builder()
                .pName(pName)
                .price(price)
                .pDesc(pDesc)
                .stock(stock)
                .category(category)
                .sellStatus(sellStatus)
                .build();
    }
}
