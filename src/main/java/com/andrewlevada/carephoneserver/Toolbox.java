package com.andrewlevada.carephoneserver;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.json.JSONObject;

public class Toolbox {
    public static String getUidFromFirebaseAuthToken(String userToken) {
        FirebaseToken decodedToken;
        try {
            decodedToken = FirebaseAuth.getInstance().verifyIdToken(userToken);
        } catch (FirebaseAuthException e) {
            return null;
        }

        return decodedToken.getUid();
    }
}
