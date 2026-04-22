package com.example.shop.service;

import com.example.shop.config.MemberAdapter;
import com.example.shop.controller.members.dto.OAuth2Attributes;
import com.example.shop.domain.Member;
import com.example.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 1. 기본 엔진(super)을 이용해 소셜에서 유저 정보를 가져옵니다.
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 2. 어느 서비스인지(google, kakao 등)와 고유 키값(sub, id 등)을 추출합니다.
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // 3. 소셜마다 다른 규격을 OAuth2Attributes 하나로 통일합니다.
        OAuth2Attributes attributes = OAuth2Attributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        // 4. DB에 저장하거나 정보를 업데이트합니다.
        Member member = saveOrUpdate(attributes);

        // 5. 성공한 유저 정보를 '세션 주머니'에 넣을 수 있게 MemberAdapter로 감싸서 반환합니다.
        return new MemberAdapter(member, oAuth2User.getAttributes());
    }

    private Member saveOrUpdate(OAuth2Attributes attributes) {
        Member member = memberRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName())) // 이미 있으면 이름만 업데이트
                .orElse(attributes.toEntity()); // 없으면 새로 생성 (가입)

        return memberRepository.save(member);
    }
}