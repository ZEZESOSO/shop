package com.example.shop.controller.members;

import com.example.shop.config.MemberAdapter;
import com.example.shop.domain.CartItem;
import com.example.shop.domain.Wishlist;
import com.example.shop.service.CartService;
import com.example.shop.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MyPageController {

    private final WishlistService wishlistService;
    private final CartService cartService;

    @GetMapping("/members/myPage")
    public String myPage(@AuthenticationPrincipal MemberAdapter memberAdapter, Model model) {
        Long memberId = memberAdapter.getMember().getId();
        List<Wishlist> wishlist = wishlistService.getMyWishlist(memberId);
        List<CartItem> cartItems = cartService.getMyCart(memberId);
        model.addAttribute("wishlist", wishlist);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("memberName", memberAdapter.getMember().getName());
        return "myPage";
    }
}

