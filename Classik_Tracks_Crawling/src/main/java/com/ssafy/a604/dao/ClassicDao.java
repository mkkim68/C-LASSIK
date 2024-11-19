package com.ssafy.a604.dao;

import com.ssafy.a604.config.YmlConfigLoader;
import com.ssafy.a604.entity.Classic;
import com.ssafy.a604.entity.Composer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClassicDao {

    private static final String DB_URL = YmlConfigLoader.getDbUrl();
    private static final String DB_USER = YmlConfigLoader.getDbUser();
    private static final String DB_PASSWORD = YmlConfigLoader.getDbPassword();

    public void saveClassic(Classic classic) {
        String query = "INSERT INTO track (title, composer_id, tags, video_id) VALUES (?, ?, ?, ?)";
        try (
                Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement pstmt = conn.prepareStatement(query)
        ) {
            pstmt.setString(1, classic.getTitle());
            pstmt.setInt(2, getComposerIdByName(classic.getComposer()));
            pstmt.setString(3, classic.getTags());
            pstmt.setString(4, classic.getVideoId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Data Insert Error: " + e.getMessage());
        }
    }

    public Integer getComposerIdByName(String composerName) {
        String query = "SELECT composer_id FROM composer WHERE name = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, composerName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("composer_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveComposer(Composer composer) {
        String query = "INSERT INTO composer (name, description) VALUES (?, ?)";
        try (
                Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement pstmt = conn.prepareStatement(query)
        ) {
            pstmt.setString(1, composer.getName());
            pstmt.setString(2, composer.getDescription());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Data Insert Error: " + e.getMessage());
        }
    }

}