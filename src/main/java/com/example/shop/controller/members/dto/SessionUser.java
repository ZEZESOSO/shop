package com.example.shop.controller.members.dto;

import com.example.shop.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class SessionUser implements Serializable {
    private static final long serialVersionUID = 1L; // 권장 (버전 관리용)
    private String name;
    private String email;

    public SessionUser(Member member) {
        this.name = member.getName();
        this.email = member.getEmail();
    }
}
