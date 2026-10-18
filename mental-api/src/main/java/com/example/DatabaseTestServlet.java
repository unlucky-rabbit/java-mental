
package com.example;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/db-test")
public class DatabaseTestServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        response.setContentType("application/json;charset=UTF-8");

        try (
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT 1")
        ) {
            if (resultSet.next() && resultSet.getInt(1) == 1) {
                response.getWriter().write(
                    "{\"status\":\"ok\",\"message\":\"PostgreSQL接続成功\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write(
                    "{\"status\":\"error\",\"message\":\"SQL実行結果が不正です\"}");
            }

        } catch (Exception e) {
            getServletContext().log("PostgreSQL接続テストに失敗しました", e);

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(
                "{\"status\":\"error\",\"message\":\"PostgreSQL接続失敗。Tomcatログを確認してください\"}");
        }
    }
}
