package com.example.shop.domain;

import com.example.shop.controller.product.dto.ProductUpdateRequest;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="product")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Product {

    @Id
    @Column(name="product_id") // DB 컬럼명은 명확하게, 변수명은 id로
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String pName;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false)
    private String category;

    @Lob
    @Column(nullable = false)
    private String pDesc; // itemDetail -> pDesc로 변경

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductImage> productImgFiles = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private SellStatus sellStatus; // 상품 판매 상태 (SELL, SOLD_OUT)

    // 업데이트 로직
    public void update(ProductUpdateRequest request) {
        this.pName = request.getPName();
        this.price = request.getPrice();
        this.pDesc = request.getPDesc();
        this.stock = request.getStock();
        this.category = request.getCategory();

        // 화면에서 넘어온 상태값이 있다면 반영
        if(request.getSellStatus() != null) {
            this.sellStatus = request.getSellStatus();
        }
    }
}