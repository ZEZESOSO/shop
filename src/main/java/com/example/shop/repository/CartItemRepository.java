package com.example.shop.repository;

import com.example.shop.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByMemberIdAndProductId(Long memberId, Long productId);

    @Query("""
            select ci
            from CartItem ci
            join fetch ci.product p
            where ci.member.id = :memberId
            order by ci.id desc
            """)
    List<CartItem> findAllByMemberIdWithProduct(@Param("memberId") Long memberId);
}

