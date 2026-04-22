package com.example.shop.service;

import com.example.shop.domain.Member;
import com.example.shop.domain.Product;
import com.example.shop.domain.Wishlist;
import com.example.shop.repository.MemberRepository;
import com.example.shop.repository.ProductRepository;
import com.example.shop.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    /**
     * 특정 상품의 찜 여부 확인 (메인 화면용)
     */
    public boolean isLiked(Long memberId, Long productId) {
        if (memberId == null) return false; // 로그인 안 한 사용자는 무조건 false
        return wishlistRepository.existsByMemberIdAndProductId(memberId, productId);
    }

    /**
     * 좋아요 토글 (찜 하기 / 취소 하기)
     */
    @Transactional
    public boolean toggleWishlist(Long memberId, Long productId) {
        // 1. 회원과 상품 존재 여부 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 없습니다. id=" + memberId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다. id=" + productId));

        // 2. 이미 찜했는지 확인
        Optional<Wishlist> wishlist = wishlistRepository.findByMemberIdAndProductId(memberId, productId);

        if (wishlist.isPresent()) {
            // 3. 이미 있다면 삭제 (찜 취소)
            wishlistRepository.delete(wishlist.get());
            return false;
        } else {
            // 4. 없다면 새로 저장 (찜 하기)
            Wishlist newWishlist = Wishlist.builder()
                    .member(member)
                    .product(product)
                    .build();
            wishlistRepository.save(newWishlist);
            return true;
        }
    }

    /**
     * 마이페이지: 내 찜목록(상품 리스트) 조회
     */
    public List<Wishlist> getMyWishlist(Long memberId) {
        return wishlistRepository.findAllByMemberIdWithProduct(memberId);
    }
}