package com.example.shop.service;

import com.example.shop.domain.CartItem;
import com.example.shop.domain.Member;
import com.example.shop.domain.Product;
import com.example.shop.repository.CartItemRepository;
import com.example.shop.repository.MemberRepository;
import com.example.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    /**
     * 장바구니 담기:
     * - 이미 담겨 있으면 수량 증가
     * - 없으면 신규 생성
     */
    @Transactional
    public CartItem addToCart(Long memberId, Long productId, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 없습니다. id=" + memberId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다. id=" + productId));

        CartItem item = cartItemRepository.findByMemberIdAndProductId(memberId, productId)
                .orElse(null);

        if (item == null) {
            CartItem created = CartItem.builder()
                    .member(member)
                    .product(product)
                    .quantity(quantity)
                    .build();
            return cartItemRepository.save(created);
        }

        item.increase(quantity);
        return item;
    }

    @Transactional
    public CartItem changeQuantity(Long memberId, Long cartItemId, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 장바구니 아이템이 없습니다. id=" + cartItemId));
        if (!item.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("본인 장바구니만 수정할 수 있습니다.");
        }
        item.changeQuantity(quantity);
        return item;
    }

    @Transactional
    public void remove(Long memberId, Long cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 장바구니 아이템이 없습니다. id=" + cartItemId));
        if (!item.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("본인 장바구니만 삭제할 수 있습니다.");
        }
        cartItemRepository.delete(item);
    }

    public List<CartItem> getMyCart(Long memberId) {
        return cartItemRepository.findAllByMemberIdWithProduct(memberId);
    }
}
