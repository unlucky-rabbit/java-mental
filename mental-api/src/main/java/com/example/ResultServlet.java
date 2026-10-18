
package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/results")
public class ResultServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    // Angularからの通信に必要なCORSヘッダー
    private void setCorsHeaders(HttpServletResponse response) {
        response.setHeader(
            "Access-Control-Allow-Origin",
            "http://localhost:4200"
        );
        response.setHeader(
            "Access-Control-Allow-Methods",
            "POST, OPTIONS"
        );
        response.setHeader(
            "Access-Control-Allow-Headers",
            "Content-Type"
        );
    }

    // ブラウザによる事前確認に応答
    @Override
    protected void doOptions(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        setCorsHeaders(response);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    // 回答データを受信
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        setCorsHeaders(response);
        request.setCharacterEncoding("UTF-8");

        response.setContentType("application/json;charset=UTF-8");

        JsonNode answers;
        try (BufferedReader reader = request.getReader()) {
            JsonNode requestBody = OBJECT_MAPPER.readTree(reader);
            answers = requestBody == null ? null : requestBody.get("answers");
        } catch (IOException e) {
            writeError(response, HttpServletResponse.SC_BAD_REQUEST, "リクエストJSONを読み取れません");
            return;
        }

        if (answers == null || !answers.isArray() || answers.isEmpty()) {
            writeError(response, HttpServletResponse.SC_BAD_REQUEST, "answers配列が必要です");
            return;
        }

        for (JsonNode answer : answers) {
            JsonNode questionId = answer.get("questionId");
            JsonNode answerValue = answer.get("answer");
            if (questionId == null || !questionId.isIntegralNumber()
                    || !questionId.canConvertToInt() || questionId.intValue() <= 0
                    || answerValue == null
                    || !(answerValue.isTextual() || answerValue.isNumber())
                    || (answerValue.isTextual() && answerValue.asText().isBlank())) {
                writeError(response, HttpServletResponse.SC_BAD_REQUEST, "回答データの形式が不正です");
                return;
            }
        }

        String sql = "INSERT INTO answers (question_id, answer) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                for (JsonNode answer : answers) {
                    statement.setInt(1, answer.get("questionId").intValue());
                    statement.setString(2, answer.get("answer").asText());
                    statement.addBatch();
                }

                int[] results = statement.executeBatch();
                for (int result : results) {
                    if (result == Statement.EXECUTE_FAILED) {
                        throw new SQLException("回答データの保存に失敗しました");
                    }
                }
                connection.commit();
            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    e.addSuppressed(rollbackException);
                }
                throw e;
            }
        } catch (SQLException e) {
            getServletContext().log("回答データの保存に失敗しました", e);
            writeError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "回答の保存に失敗しました");
            return;
        }

        response.getWriter().write(
            "{\"status\":\"ok\",\"saved\":" + answers.size() + "}"
        );
    }

    private void writeError(
            HttpServletResponse response,
            int status,
            String message) throws IOException {
        response.setStatus(status);
        response.getWriter().write(
            OBJECT_MAPPER.writeValueAsString(
                java.util.Map.of("status", "error", "message", message)
            )
        );
    }
}