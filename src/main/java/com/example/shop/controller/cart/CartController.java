package com.example.shop.controller.cart;

import com.example.shop.config.MemberAdapter;
import com.example.shop.domain.CartItem;
import com.example.shop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping("/{productId}")
    public ResponseEntity<CartItem> add(@AuthenticationPrincipal MemberAdapter memberAdapter,
                                        @PathVariable Long productId,
                                        @RequestBody Map<String, Object> body) {
        Long memberId = memberAdapter.getMember().getId();
        int quantity = (body.get("quantity") == null) ? 1 : ((Number) body.get("quantity")).intValue();
        return ResponseEntity.ok(cartService.addToCart(memberId, productId, quantity));
    }

    @PatchMapping("/{cartItemId}")
    public ResponseEntity<CartItem> changeQuantity(@AuthenticationPrincipal MemberAdapter memberAdapter,
                                                   @PathVariable Long cartItemId,
                                                   @RequestBody Map<String, Object> body) {
        Long memberId = memberAdapter.getMember().getId();
        int quantity = ((Number) body.get("quantity")).intValue();
        return ResponseEntity.ok(cartService.changeQuantity(memberId, cartItemId, quantity));
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Map<String, Object>> remove(@AuthenticationPrincipal MemberAdapter memberAdapter,
                                                      @PathVariable Long cartItemId) {
        Long memberId = memberAdapter.getMember().getId();
        cartService.remove(memberId, cartItemId);
        return ResponseEntity.ok(Map.of("deleted", true));
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> myCart(@AuthenticationPrincipal MemberAdapter memberAdapter) {
        Long memberId = memberAdapter.getMember().getId();
        return ResponseEntity.ok(cartService.getMyCart(memberId));
    }
}

