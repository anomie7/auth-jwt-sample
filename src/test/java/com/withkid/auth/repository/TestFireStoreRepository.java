package com.withkid.auth.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Ignore
public class TestFireStoreRepository {

    @Autowired
    private FirestoreRepository firestoreRepository;

    @Test
    public void getFireStore() throws IOException, ExecutionException, InterruptedException {
        Firestore db = firestoreRepository.getFireStore();

        Map<String, Object> data = new HashMap<>();
        data.put("accessToken", "adfasdfsad222f");
        data.put("refreshToken", "dafusadf");
        DocumentReference docRef = db.collection("temporary-login-token").document("ee");
        ApiFuture<WriteResult> result = docRef.set(data);
        System.out.println(result.get().getUpdateTime());
    }
}
