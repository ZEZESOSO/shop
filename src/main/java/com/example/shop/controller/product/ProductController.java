package com.example.shop.controller.product;

import com.example.shop.controller.product.dto.ProductCreateRequest;
import com.example.shop.controller.product.dto.ProductResponse;
import com.example.shop.controller.product.dto.ProductUpdateRequest;
import com.example.shop.domain.Product;
import com.example.shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/products") // 관리자 전용임을 명확히 함
public class ProductController {

    private final ProductService productService;

    // 상품 신규 등록
    @PostMapping("/register")
    public ResponseEntity<Long> save(@ModelAttribute ProductCreateRequest request) throws Exception {
        Long productId = productService.saveProduct(request);
        return ResponseEntity.ok(productId);
    }

    // 상품 수정 (이미지 순서/파일 포함 통합 수정)
    // HTML AJAX에서 "/api/admin/products/update"로 쏘면 됩니다.
    @PostMapping("/update")
    public ResponseEntity<Long> update(@ModelAttribute ProductUpdateRequest request) throws Exception {
        Long productId = productService.updateProduct(request);
        return ResponseEntity.ok(productId);
    }

    // 상품 상세 조회 (수정 화면 로딩용)
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        ProductResponse response = productService.getProduct(id);
        return ResponseEntity.ok(response);
    }

}