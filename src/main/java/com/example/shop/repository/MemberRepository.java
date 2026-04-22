package com.example.shop.repository;

import com.example.shop.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // 소셜 로그인으로 가져온 이메일이 이미 우리 DB에 있는지 확인하기 위해 필요합니다.
    Optional<Member> findByEmail(String email);
}