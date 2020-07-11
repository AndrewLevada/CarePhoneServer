package com.andrewlevada.carephoneserver.logic;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CaredUser2 {
    public String uid;
    public String phone;
    public boolean isPro;

    public CaredUser2(String uid, String phone, boolean isPro) {
        this.uid = uid;
        this.phone = phone;
        this.isPro = isPro;
    }

    public static class Mapper implements RowMapper<CaredUser2> {
        @Override
        public CaredUser2 mapRow(ResultSet resultSet, int i) throws SQLException {
            String phone;
            try {
                phone = FirebaseAuth.getInstance().getUser(resultSet.getString("uid")).getPhoneNumber();
            } catch (FirebaseAuthException e) {
                throw new SQLException("Firebase auth failed!");
            }

            return new CaredUser2(resultSet.getString("uid"), phone, resultSet.getBoolean("is_pro"));
        }
    }
}
