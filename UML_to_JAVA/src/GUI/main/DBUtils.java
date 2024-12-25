package GUI.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBUtils {
    private static final String URL = "jdbc:postgresql://localhost/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "13022005";

    // Method to validate user credentials
    public static boolean connect(String username, String password) throws SQLException {
        String query = "SELECT * FROM uml.users WHERE username = ? AND password = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set parameters for the query
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            try (ResultSet find = preparedStatement.executeQuery()) {
                if (find.next()) {
                    System.out.println("User found!");
                    return true;
                } else {
                    System.out.println("No user found with the given credentials.");
                    return false;
                }
            }

        } catch (SQLException e) {
            System.err.println("Database connection or query error: " + e.getMessage());
            throw e;
        }
    }
    
    public static void register(String username, String password) throws SQLException {
        String checkQuery = "SELECT * FROM uml.users WHERE username = ?";
        String insertQuery = "INSERT INTO uml.users (username, password) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
             PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {

            // Check if username or password already exists
            checkStatement.setString(1, username);
            try (ResultSet resultSet = checkStatement.executeQuery()) {
                if (resultSet.next()) {
                    throw new SQLException("Username already exists.");
                }
            }

            // Insert new user
            insertStatement.setString(1, username);
            insertStatement.setString(2, password);

            int rowsAffected = insertStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to insert user.");
            }
        } catch (SQLException e) {
            System.err.println("Error during registration: " + e.getMessage());
            throw e;
        }
    }
    public static void saveCode(int userId, String codeContent) throws SQLException {
        String insertQuery = "INSERT INTO uml.code (user_id, code_content) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, codeContent);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Code saved successfully.");
            } else {
                throw new SQLException("Failed to save code.");
            }
        }
    }
    public static List<String> getCodes(int userId) throws SQLException {
        String query = "SELECT code_content FROM uml.code WHERE user_id = ?";
        List<String> codes = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    codes.add(resultSet.getString("code_content"));
                }
            }
        }

        return codes;
    }
    public static int getUserId(String username, String password) throws SQLException {
        String query = "SELECT id FROM uml.users WHERE username = ? AND password = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                } else {
                    throw new SQLException("User not found.");
                }
            }
        }
    }
}