package com.withkid.auth.service;

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

@Service
@NoArgsConstructor
public class FacebookService {
    @Value("${spring.social.facebook.scope}")
    String scope;

    @Value("${spring.social.facebook.redirect-url}")
    String redirUrl;

    private FacebookConnectionFactory connectionFactory;
    private OAuth2Operations oauthOperations;

    @Inject
    public FacebookService(FacebookConnectionFactory connectionFactory, OAuth2Operations oauthOperations) {
        this.connectionFactory = connectionFactory;
        this.oauthOperations = oauthOperations;
    }

    public String createFacebookAuthorizationURL(){
        OAuth2Parameters params = new OAuth2Parameters();
        params.setRedirectUri(redirUrl);
        params.setScope(scope);
        return oauthOperations.buildAuthorizeUrl(params);
    }

    public User getUserProfile(String code){
        String facebookAccessToken = this.createFacebookAccessToken(code);
        Facebook facebook = new FacebookTemplate(facebookAccessToken);
        UserOperations userOperations = facebook.userOperations();
        String [] fields = { "id", "email",  "name", "picture", "birthday"};
        User profile = facebook.fetchObject
                ("me", User.class, fields);
        return profile;
    }

    public String createFacebookAccessToken(String code) {
        AccessGrant accessGrant = connectionFactory.getOAuthOperations().exchangeForAccess(code, redirUrl, null);
        return accessGrant.getAccessToken();
    }
}
