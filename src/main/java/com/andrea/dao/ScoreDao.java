package com.andrea.dao;

import com.andrea.dto.AccountScoreDto;
import com.andrea.model.Score;
import com.andrea.utility.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ScoreDao {

    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public int addScore(Score score) {
        String query = "INSERT INTO score (id_quiz, id_account, score, date) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setInt(1, score.getId_quiz());
            stm.setInt(2, score.getId_account());
            stm.setInt(3, score.getScore());
            stm.setObject(4, score.getDate());

            int affectedRows = stm.executeUpdate();

            if (affectedRows > 0) {
                return 1;
            } else {
                return -1;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Score> getScoreByAccountQuiz(int id_account, int id_quiz) {
        String query = """
                SELECT *
                FROM score
                WHERE id_account = ? AND id_quiz = ?;
                """;

        try {
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setInt(1, id_account);
            stm.setInt(2, id_quiz);

            ResultSet rs = stm.executeQuery();

            List<Score> list = new ArrayList<>();
            while (rs.next()) {
                Score score = new Score();
                score.setId_score(rs.getInt("id_score"));
                score.setId_account(rs.getInt("id_account"));
                score.setId_quiz(rs.getInt("id_quiz"));
                score.setScore(rs.getInt("score"));
                score.setDate(rs.getObject("date", LocalDate.class));

                list.add(score);
            }

            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<AccountScoreDto> getScore5Account(int id_course) {
        String query = """
                SELECT
                    a.id_account,
                    a.name,
                    a.last_name,
                    cr.email,
                    st.avatar,
                    AVG(s.score) AS average_score
                FROM
                    Score s
                JOIN
                    Account a ON s.id_account = a.id_account
                JOIN
                    Settingaccount st ON a.id_account = st.id_account
                JOIN
                    Credential cr ON a.id_account = cr.id_account
                JOIN
                    Quiz q ON s.id_quiz = q.id_quiz
                JOIN
                    Subject sub ON q.id_subject = sub.id_subject
                JOIN
                    Course c ON sub.id_course = c.id_course
                WHERE
                    c.id_course = ?
                GROUP BY
                    a.id_account, a.name, a.last_name, cr.email, st.avatar
                ORDER BY
                    average_score DESC
                LIMIT 5;
                
                """;
        try {
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setInt(1, id_course);

            ResultSet rs = stm.executeQuery();
            List<AccountScoreDto> list = new ArrayList<>();
            while (rs.next()) {
                AccountScoreDto account = new AccountScoreDto();
                account.setId_account(rs.getInt("id_account"));
                account.setName(rs.getString("name"));
                account.setLast_name(rs.getString("last_name"));
                account.setEmail(rs.getString("email"));
                account.setScore(rs.getDouble("average_score"));
                account.setAvatar(rs.getInt("avatar"));

                list.add(account);
            }

            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
