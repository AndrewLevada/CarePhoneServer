package com.andrewlevada.carephoneserver.logic;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CaredUser {
    public String uid;
    public String phone;

    public CaredUser(String uid, String phone) {
        this.uid = uid;
        this.phone = phone;
    }

    public static class Mapper implements RowMapper<CaredUser> {
        @Override
        public CaredUser mapRow(ResultSet resultSet, int i) throws SQLException {
            String phone;
            try {
                phone = FirebaseAuth.getInstance().getUser(resultSet.getString("uid")).getPhoneNumber();
            } catch (FirebaseAuthException e) {
                throw new SQLException("Firebase auth failed!");
            }

            return new CaredUser(resultSet.getString("uid"), phone);
        }
    }
}
