package com.example.shop.repository;

import com.example.shop.domain.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

// [설명] JpaRepository<엔티티타입, ID타입>을 상속받으면 기본적인 CRUD 메서드가 자동 생성됨
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
}