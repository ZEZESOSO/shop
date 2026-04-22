package com.example.shop.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public Member(String name, String email, Role role) {
        this.name = name;
        this.email = email;
        this.role = role;
    }

    // 소셜 정보가 변경되었을 때 이름과 이메일을 업데이트하는 비즈니스 로직
    // Role까지 업데이트해 버리면 사용자가 직접 결제해서 얻은 'VIP' 권한이 다시 'USER'로 초기화될 위험
public Member update(String name, String email) {
    this.name = name;
    // 이메일 변경이 허용되는 정책!
    if (email != null) {
        this.email = email;
    }
    return this;
}
// Spring Security(스프링 시큐리티)랑 같이 사용할 때 꼭 필요! 권한 인증을 위한 암호 
    public String getRoleKey() {
        return this.role.getKey();
    }
}