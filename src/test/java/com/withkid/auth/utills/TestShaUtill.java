package com.withkid.auth.utills;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TestShaUtill {

    @Test
    public void sha256() throws NoSuchAlgorithmException {
        String password = "1234";
        String result = ShaUtill.sha256(password);

        System.out.println(result);
        assertEquals(false, result.equals(password));
    }
}