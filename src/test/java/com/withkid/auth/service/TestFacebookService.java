package com.withkid.auth.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TestFacebookService {
    @Autowired
    private FacebookService facebookService;

    @Test
    public void testGetAuthorizeUrl(){
        String url = facebookService.createFacebookAuthorizationURL();
        System.out.println(url);
    }
}