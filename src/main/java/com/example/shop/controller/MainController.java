package com.example.shop.controller;

import com.example.shop.config.MemberAdapter;
import com.example.shop.controller.product.dto.ProductMainDto;
import com.example.shop.service.ProductService;
import com.example.shop.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ProductService productService;
    private final WishlistService wishlistService;


    @GetMapping("/")
    public String main(@AuthenticationPrincipal MemberAdapter memberAdapter,
                       @RequestParam(value = "category", required = false) String category,
                       Model model) {

        // 1. 로그인 유저 ID 가져오기 (비로그인 시 null)
        Long memberId = (memberAdapter != null) ? memberAdapter.getMember().getId() : null;

        // 2. 서비스 호출: 카테고리와 유저 ID(위시리스트 확인용)를 넘깁니다.
        List<ProductMainDto> products = productService.getProductsForMain(category, memberId);

        // 3. 뷰에 데이터 전달
        model.addAttribute("products", products);
        model.addAttribute("selectedCategory", category); // 현재 선택된 카테고리 표시용
        return "shopMain"; // templates/shopMain.html
    }
}


