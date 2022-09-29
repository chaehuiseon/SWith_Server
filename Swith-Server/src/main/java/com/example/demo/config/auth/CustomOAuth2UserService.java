package com.example.demo.config.auth;

import com.example.demo.config.BaseException;
import com.example.demo.config.auth.OAuthAttributes;
import com.example.demo.config.auth.SessionUser;
import com.example.demo.src.entity.User;
import com.example.demo.src.enums.RoleType;
import com.example.demo.src.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextListener;

import javax.servlet.http.HttpSession;
import java.util.Collections;

import static com.example.demo.config.BaseResponseStatus.POST_USERS_EXISTS_EMAIL;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Bean
    public RequestContextListener requestContextListener(){
        return new RequestContextListener();
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        
        // OAuth2 서비스 id (구글, 카카오, 네이버)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // OAuth2 로그인 진행 시 키가 되는 필드 값(PK)
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();
        // OAuth2UserService
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        User user = saveOrUpdate(attributes);

        // 로그 출력
//        try {
//            System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(userRequest));
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }

        // SessionUser (직렬화된 dto 클래스 사용)
        httpSession.setAttribute("user", new SessionUser(user));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }


    private User saveOrUpdate(OAuthAttributes attributes) {
        // 이미 가입된 경우
        if(userRepository.existsByEmail(attributes.getEmail())){
            System.out.println("이미 가입된 이메일");
            User user = userRepository.findByEmail(attributes.getEmail());
            User updateUser = user.builder()
                    .email(attributes.getEmail())
                    .nickname(attributes.getName())
                    .role(RoleType.GUEST)
                    .profileImgUrl(attributes.getPicture())
                    .build();
            return updateUser;
        }
        else{
            // todo : 가입 시 필요한 정보 입력 페이지로 이동
            User user = userRepository.findByEmail(attributes.getEmail());
            User savedUser = user.builder()
                    .email(attributes.getEmail())
                    .nickname(attributes.getName())
                    .role(RoleType.GUEST)
                    .profileImgUrl(attributes.getPicture())
//                .status(0)
                    .build();
            return userRepository.save(savedUser);
        }

//        User user = userRepository.findByEmail(attributes.getEmail())
//                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
//                .orElse(attributes.toEntity());
//        return userRepository.save(user);

    }
}