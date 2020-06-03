package com.andrewlevada.carephoneserver;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.json.JSONObject;

public class Toolbox {
    public static String getUidFromFirebaseAuthToken(JSONObject body) {
        FirebaseToken decodedToken;
        try {
            decodedToken = FirebaseAuth.getInstance().verifyIdToken(body.getString("user_token"));
        } catch (FirebaseAuthException e) {
            return null;
        }

        return decodedToken.getUid();
    }
}
