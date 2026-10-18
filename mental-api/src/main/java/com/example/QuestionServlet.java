package com.example;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet("/api/questions")
public class QuestionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 【超重要】CORS対策：Angular（ポート4200）からのアクセスを許可する
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        response.setContentType("application/json;charset=UTF-8");

        // メンタルヘルステストの簡易ダミーデータ（配列）
        String[] questions = {
            "最近、よく眠れていますか？",
            "仕事や勉強に集中できていますか？",
            "理由もなく不安になることがありますか？"
        };

        // Jacksonライブラリを使って、Javaの配列を自動でJSONテキストに変換して返却
        ObjectMapper mapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        out.print(mapper.writeValueAsString(questions));
        out.flush();
    }
}
