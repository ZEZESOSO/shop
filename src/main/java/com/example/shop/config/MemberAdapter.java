package com.example.shop.config;

import com.example.shop.domain.Member;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collections;
import java.util.Map;

@Getter
public class MemberAdapter extends DefaultOAuth2User {
    private final Member member;

    public MemberAdapter(Member member, Map<String, Object> attributes) {
        super(Collections.singleton(new SimpleGrantedAuthority(member.getRoleKey())),
                attributes,
                "email"); // 구글/카카오 등의 구별값 키 (보통 email 혹은 sub)
        this.member = member;
    }
}