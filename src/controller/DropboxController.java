package controller;

import model.Dropbox;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DropboxController {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/tubes_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public List<Dropbox> getAllDropboxes() throws SQLException {
        List<Dropbox> dropboxes = new ArrayList<>();
        String sql = "SELECT * FROM drop_box";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Dropbox dropbox = new Dropbox(
                        resultSet.getInt("id"),
                        resultSet.getString("nama_dropbox"),
                        resultSet.getString("kapasitas"),
                        resultSet.getString("status"),
                        resultSet.getString("alamat")
                );
                dropboxes.add(dropbox);
            }
        }
        return dropboxes;
    }

    public void addDropbox(Dropbox dropbox) throws SQLException {
        String sql = "INSERT INTO drop_box (id, nama_dropbox, kapasitas, status, alamat) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, dropbox.getId());
            preparedStatement.setString(2, dropbox.getNamaDropbox());
            preparedStatement.setString(3, dropbox.getKapasitas());
            preparedStatement.setString(4, dropbox.getStatus());
            preparedStatement.setString(5, dropbox.getAlamat());
            preparedStatement.executeUpdate();
        }
    }

    public void updateDropbox(Dropbox dropbox) throws SQLException {
        String sql = "UPDATE drop_box SET nama_dropbox = ?, kapasitas = ?, status = ?, alamat = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, dropbox.getNamaDropbox());
            preparedStatement.setString(2, dropbox.getKapasitas());
            preparedStatement.setString(3, dropbox.getStatus());
            preparedStatement.setString(4, dropbox.getAlamat());
            preparedStatement.setInt(5, dropbox.getId());
            preparedStatement.executeUpdate();
        }
    }

    public void deleteDropbox(int id) throws SQLException {
        String sql = "DELETE FROM drop_box WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }
}

