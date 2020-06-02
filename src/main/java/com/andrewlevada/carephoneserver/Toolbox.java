package com.andrewlevada.carephoneserver;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

public class Toolbox {
    public static String getUidFromFirebaseAuthToken(String token) {
        FirebaseToken decodedToken;
        try {
            decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
        } catch (FirebaseAuthException e) {
            return null;
        }

        return decodedToken.getUid();
    }
}
