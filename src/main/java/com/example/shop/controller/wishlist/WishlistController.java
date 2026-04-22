package com.example.shop.controller.wishlist;

import com.example.shop.config.MemberAdapter;
import com.example.shop.domain.Wishlist;
import com.example.shop.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/{productId}/toggle")
    public ResponseEntity<Map<String, Object>> toggle(@AuthenticationPrincipal MemberAdapter memberAdapter,
                                                     @PathVariable Long productId) {
        Long memberId = memberAdapter.getMember().getId();
        boolean liked = wishlistService.toggleWishlist(memberId, productId);
        return ResponseEntity.ok(Map.of("liked", liked));
    }

    @GetMapping
    public ResponseEntity<List<Wishlist>> myWishlist(@AuthenticationPrincipal MemberAdapter memberAdapter) {
        Long memberId = memberAdapter.getMember().getId();
        return ResponseEntity.ok(wishlistService.getMyWishlist(memberId));
    }
}

