package com.example.shop.repository;

import com.example.shop.domain.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    // 특정 멤버가 특정 상품을 찜했는지 존재 여부 확인
    boolean existsByMemberIdAndProductId(Long memberId, Long productId);

    // 찜 삭제를 위해 특정 멤버와 상품 조합으로 데이터 찾기
    Optional<Wishlist> findByMemberIdAndProductId(Long memberId, Long productId);

    // 마이페이지 찜목록 조회 (상품까지 같이 가져오기)
    @Query("""
            select w
            from Wishlist w
            join fetch w.product p
            where w.member.id = :memberId
            order by w.id desc
            """)
    List<Wishlist> findAllByMemberIdWithProduct(@Param("memberId") Long memberId);
}