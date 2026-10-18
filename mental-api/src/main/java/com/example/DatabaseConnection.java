
package com.example;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DatabaseConnection {

    public static Connection getConnection()
            throws SQLException {

        try {
            Context context = new InitialContext();

            DataSource dataSource = (DataSource) context.lookup(
                    "java:comp/env/jdbc/MentalDB");

            return dataSource.getConnection();

        } catch (NamingException e) {
            throw new SQLException(
                    "PostgreSQLの接続設定を取得できません。"
                    + "context.xmlを確認してください。",
                    e);
        }
    }
}
/* 環境変数を使った接続方法（参考）
public class DatabaseConnection {

    private static final String URL =
            "jdbc:postgresql://localhost:5432/mental_db";

    private static final String USER = "postgres";

    public static Connection getConnection() throws SQLException {
        String password = System.getenv("MENTAL_DB_PASSWORD");

        if (password == null || password.isBlank()) {
            throw new SQLException(
                    "環境変数 MENTAL_DB_PASSWORD が設定されていません。");
        }

        return DriverManager.getConnection(URL, USER, password);
    }
}
*/