package controller;

import model.JenisSampah;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class JenisSampahController {
    private Connection connection;

    public JenisSampahController() {
        try {
            String url = "jdbc:mysql://localhost:3306/tubes_db"; 
            String user = "root"; 
            String password = ""; 
            connection = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<JenisSampah> getAllJenisSampah() {
        List<JenisSampah> jenisSampahList = new ArrayList<>();
        String query = "SELECT id, nama_jenis_sampah, total_berat FROM jenis_sampah";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String namaJenis = resultSet.getString("nama_jenis_sampah");
                String totalBerat = resultSet.getString("total_berat");

                JenisSampah jenisSampah = new JenisSampah(id, namaJenis, totalBerat);
                jenisSampahList.add(jenisSampah);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jenisSampahList;
    }

    public boolean addJenisSampah(JenisSampah jenisSampah) {
        String query = "INSERT INTO jenis_sampah (nama_jenis_sampah, total_berat) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, jenisSampah.getNamaJenis());
            statement.setString(2, jenisSampah.getTotalBerat());
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateJenisSampah(JenisSampah jenisSampah) {
        String query = "UPDATE jenis_sampah SET nama_jenis_sampah = ?, total_berat = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, jenisSampah.getNamaJenis());
            statement.setString(2, jenisSampah.getTotalBerat());
            statement.setInt(3, jenisSampah.getId());
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteJenisSampah(int id) {
        String query = "DELETE FROM jenis_sampah WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
