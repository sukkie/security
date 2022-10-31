package com.cos.security.config.oauth;

import com.cos.security.config.auth.PrincipalDetails;
import com.cos.security.config.oauth.provider.FacebookUserInfo;
import com.cos.security.config.oauth.provider.GoogleUserInfo;
import com.cos.security.config.oauth.provider.NaverUserInfo;
import com.cos.security.config.oauth.provider.OAuth2UserInfo;
import com.cos.security.model.UserModel;
import com.cos.security.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    // FB으로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
    // 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어 진다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("getClientRegistration : " + userRequest.getClientRegistration()); // registrationId로 어떤 OAuth인지 알 수 있음
        log.info("getAccessToken : " + userRequest.getAccessToken().getTokenValue());

        // userRequest 정보를 통해서 loadUser함수 호출, 결과로 회원 프로필 정보 취득.
        OAuth2User oauth2User = super.loadUser(userRequest);
        log.info("getAuthorities : " + oauth2User.getAuthorities());

        // 회원 가입 진행
        OAuth2UserInfo oAuth2UserInfo = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            oAuth2UserInfo = new GoogleUserInfo(oauth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
            oAuth2UserInfo = new FacebookUserInfo(oauth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            oAuth2UserInfo = new NaverUserInfo(oauth2User.getAttribute("response"));
        } else {
            log.error("only support facebook or google or naver");
        }

        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String username = provider + "_" + providerId;
        String password = bCryptPasswordEncoder.encode("test");
        String email = oAuth2UserInfo.getEmail();
        String role = "ROLE_USER";

        UserModel userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            userEntity = UserModel.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId).build();
            userRepository.save(userEntity);
        }

        return new PrincipalDetails(userEntity, oauth2User.getAttributes());
    }
}
