package com.example.systemcouse.repository;

import com.example.systemcouse.model.Score;
import com.example.systemcouse.model.Task;

import javax.ejb.Singleton;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class ScoreRepository {
    private static final String jdbcURL  = "jdbc:h2:~/CourseSystem";
    private static final String jdbcUsername = "swd";
    private static final String jdbcPass = "";

    private static final String INSERT_BASE_SQL = "insert into score" +
            " (score_id, score_name) VALUES (?, ?);";
    private static final String SELECT_ALL_BASE = "select * from score";
    private static final String SELECT_BASE_BY_ID = "select score_id, score_name from score where score_id=?";
    private static final String DELETE_BASE_SQL = "delete from score where score_id = ?;";
    private static final String UPDATE_BASE_SQL = "update score set score_name=? where score_id = ?;";


    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPass);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public List<Score> selectScores() {
        List<Score> scores = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_BASE)) {
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                long scoreId = rs.getLong("score_id");
                String scoreName = rs.getString("score_name");
                scores.add(new Score(scoreId, scoreName));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return scores;
    }

    public Score selectScore(int scoreId) {
        Score score = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BASE_BY_ID)) {
            preparedStatement.setInt(1, scoreId);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String rankName = rs.getString("score_name");
                score = new Score((long) scoreId, rankName);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return score;
    }


    public Score insertScore(Score score) {
        boolean check = false;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BASE_SQL)) {
            preparedStatement.setLong(1, score.getScoreId());
            preparedStatement.setString(2, score.getScoreName());
            check = preparedStatement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(check)
            return score;
        else return new Score();
    }

    public boolean deleteScore(int id) {
        boolean deleted = false;
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_BASE_SQL);) {
            statement.setLong(1, id);
            deleted = statement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return deleted;
    }

    public boolean updateScore(Score score) {
        boolean updated = false;
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_BASE_SQL);) {
            statement.setString(1, score.getScoreName());
            statement.setLong(2, score.getScoreId());

            updated = statement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return updated;
    }
}
