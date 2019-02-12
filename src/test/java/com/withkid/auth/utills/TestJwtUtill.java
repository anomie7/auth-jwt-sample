package com.withkid.auth.utills;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TestJwtUtill {

    @Test
    public void testReadJsonFile() throws IOException, JSONException {
        final String filePath = "D:\\DEV\\WITHKID\\secretkey.json";
        final String secretKey = "hDeGSSZ5SpDGsbxt_UVZktzvv6F-uPvB01sDtbm6FlA";

        String key = JwtUtill.getSecretKeyOf(filePath);
        assertEquals(secretKey, key);
    }
}