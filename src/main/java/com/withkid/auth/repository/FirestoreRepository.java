package com.withkid.auth.repository;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Transaction;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Repository
public class FirestoreRepository {

    final String keyPath = "C:\\Users\\전찬동\\Downloads\\test-fe856-21f716968770.json";
    private Firestore db;

    public FirestoreRepository() throws IOException {
        InputStream serviceAccount = new FileInputStream(keyPath);
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(credentials)
                .build();
        FirebaseApp.initializeApp(options);

        this.db = FirestoreClient.getFirestore();
    }

    public Firestore getFireStore() throws IOException {
        return db;
    }
    public void saveTokens(HashMap<String, String> tokens, String temporaryCode) throws Exception {
        DocumentReference docRef = this.db.collection("temporary-login-token").document(temporaryCode);
        db.runTransaction(
            new Transaction.Function<Void>() {
                @Override
                public Void updateCallback(Transaction transaction) throws Exception {
                    transaction.update(docRef, "accessToken", tokens.get("accessToken"));
                    transaction.update(docRef,"refreshToken", tokens.get("refreshToken"));
                    return null;
                }
        });
    }

    public Map<String, Object> findAccessTokenAndRefreshToken(String temporaryCode) throws IOException, ExecutionException, InterruptedException {
        ApiFuture<DocumentSnapshot> future = this.db.collection("temporary-login-token").document(temporaryCode).get();
        DocumentSnapshot doc = future.get();
        if(doc.exists()){
            Map<String, Object> data = doc.getData();
            return data;
        }else {
            throw new IOException("존재하지 않는 데이터입니다.");
        }
    }
}
