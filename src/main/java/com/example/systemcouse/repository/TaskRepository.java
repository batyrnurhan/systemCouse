package com.example.systemcouse.repository;

import com.example.systemcouse.model.Task;
import com.example.systemcouse.service.AllService;

import javax.ejb.Singleton;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class TaskRepository {

    private static final String jdbcURL  = "jdbc:h2:~/CourseSystem";
    private static final String jdbcUsername = "swd";
    private static final String jdbcPass = "";

    private static final String INSERT_BASE_SQL = "insert into task" +
            " (task_id, task_name) VALUES " + "(?, ?);";
    private static final String SELECT_BASE_BY_ID = "select task_name from task where task_id = ?;";
    private static final String SELECT_ALL_BASE = "select * from task;";
    private static final String DELETE_BASE_SQL = "delete from task where task_id = ?;";
    private static final String UPDATE_BASE_SQL = "update task set task_name=? where task_id = ?;";

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
    public List<Task> selectTasks() {
        List<Task> tasks = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_BASE)) {
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                long taskId = rs.getLong("task_id");
                String taskName = rs.getString("task_name");
                tasks.add(new Task(taskId, taskName));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return tasks;
    }


    public Task selectTask(int taskId) {
        Task task = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BASE_BY_ID)) {
            preparedStatement.setInt(1, taskId);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String taskName = rs.getString("task_name");
                task = new Task((long) taskId, taskName);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return task;
    }

    public Task insertTask(Task task){
        boolean check = false;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BASE_SQL)) {
            preparedStatement.setLong(1, task.getTaskId());
            preparedStatement.setString(2, task.getTaskName());
            check = preparedStatement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(check)
            return task;
        else return new Task();
    }

    public boolean deleteMap(int id) {
        boolean deleted = false;
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_BASE_SQL);) {
            statement.setLong(1, id);
            deleted = statement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return deleted;
    }

    public boolean updateTask(Task task) {
        boolean updated = false;
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_BASE_SQL);) {
            statement.setString(1, task.getTaskName());
            statement.setLong(2, task.getTaskId());

            updated = statement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return updated;
    }


}
