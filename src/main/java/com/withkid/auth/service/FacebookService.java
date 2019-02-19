package com.withkid.auth.service;

import com.withkid.auth.domain.FacebookUser;
import com.withkid.auth.repository.UserRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.UserOperations;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Optional;

@Service
@NoArgsConstructor
public class FacebookService {
    @Value("${spring.social.facebook.scope}")
    String scope;

    @Value("${spring.social.facebook.redirect-url}")
    String redirUrl;

    private FacebookConnectionFactory connectionFactory;
    private OAuth2Operations oauthOperations;
    private UserRepository userRepository;
    private JwtService jwtService;

    @Inject
    public FacebookService(FacebookConnectionFactory connectionFactory, OAuth2Operations oauthOperations, UserRepository userRepository, JwtService jwtService) {
        this.connectionFactory = connectionFactory;
        this.oauthOperations = oauthOperations;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }


    public HashMap<String, String> login(String code, String temporaryCode) throws Exception {
        org.springframework.social.facebook.api.User profile = this.getUserProfile(code, temporaryCode);
        String url = FacebookUser.getPictureUrl(profile);
        FacebookUser facebookUser = new FacebookUser(null, profile.getEmail(), profile.getName(), url, profile.getBirthday());
        FacebookUser newUser = this.signUp(facebookUser);
        return this.generateTokens(newUser);
    }

    public HashMap<String, String> generateTokens(FacebookUser user) throws Exception {
        String aceessToken = null;
        String refreshToken = null;

        HashMap<String, Object> claims = new HashMap<>();
        claims.put("user-type", "F");
        claims.put("email", user.getEmail());
        refreshToken = jwtService.createRefreshToken(claims);
        claims.put("user-id", user.getId());
        aceessToken = jwtService.createAccessToken(claims);
        HashMap<String, String> res = new HashMap<>();
        res.put("accessToken", aceessToken);
        res.put("refreshToken", refreshToken);
        return res;
    }

    public User getUserProfile(String code, String temporaryCode){
        String facebookAccessToken = this.createFacebookAccessToken(code, temporaryCode);
        Facebook facebook = new FacebookTemplate(facebookAccessToken);
        UserOperations userOperations = facebook.userOperations();
        String [] fields = { "id", "email",  "name", "picture", "birthday"};
        User profile = facebook.fetchObject
                ("me", User.class, fields);
        return profile;
    }

    public String createFacebookAccessToken(String code, String temporaryCode) {
        AccessGrant accessGrant = connectionFactory.getOAuthOperations().exchangeForAccess(code, redirUrl + temporaryCode, null);
        return accessGrant.getAccessToken();
    }

    public String createFacebookAuthorizationURL(String temporaryCode){
        OAuth2Parameters params = new OAuth2Parameters();
        params.setRedirectUri(redirUrl + temporaryCode);
        params.setScope(scope);
        return oauthOperations.buildAuthorizeUrl(params);
    }

    public FacebookUser signUp(FacebookUser user) throws Exception {
        Optional<com.withkid.auth.domain.User> findUserOpt = userRepository.findByEmail(user.getEmail());

        if(findUserOpt.isPresent()) {
            return (FacebookUser) findUserOpt.get();
        }else {
            user = userRepository.save(user);
            return user;
        }
    }
}
