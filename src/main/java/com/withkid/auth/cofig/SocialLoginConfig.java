package com.withkid.auth.cofig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.OAuth2Operations;

@Configuration
public class SocialLoginConfig {

    @Autowired
    private Environment env;

    @Bean
    public FacebookConnectionFactory facebookConnectionFactory(){
        String facebookAppId = env.getProperty("spring.social.facebook.app-id");
        String facebookSecret = env.getProperty("spring.social.facebook.app-secret");
        FacebookConnectionFactory connectionFactory = new FacebookConnectionFactory(facebookAppId, facebookSecret);
        return connectionFactory;
    }

    @Bean
    public OAuth2Operations oAuth2Operations(){
        return facebookConnectionFactory().getOAuthOperations();
    }
}
