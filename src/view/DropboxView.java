package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class DropboxView {
    @SuppressWarnings("Convert2Lambda")
    public static void open() {
        JFrame frame = new JFrame("Dropbox Management");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel mainPanel = new JPanel(new BorderLayout());

        String[] columnNames = {"ID", "Nama Dropbox", "Kapasitas", "Status", "Alamat Dropbox"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);

        // Menambahkan garis antar baris dan kolom dengan warna hitam
        table.setShowGrid(true);
        table.setGridColor(Color.BLACK);

        // Mengatur tinggi baris tanpa tambahan jarak
        table.setRowHeight(20);

        // Mengatur seleksi hanya untuk satu baris
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane tableScrollPane = new JScrollPane(table);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Tambah Data");
        JButton updateButton = new JButton("Ubah Data");
        JButton deleteButton = new JButton("Hapus Data");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Metode untuk menampilkan data dari database ke tabel
        loadTableData(tableModel);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField namaField = new JTextField();
                JTextField kapasitasField = new JTextField();
                JTextField statusField = new JTextField();
                JTextField alamatField = new JTextField();

                Object[] inputFields = {
                    "Nama Dropbox:", namaField,
                    "Kapasitas:", kapasitasField,
                    "Status:", statusField,
                    "Alamat Dropbox:", alamatField
                };

                int option = JOptionPane.showConfirmDialog(frame, inputFields, "Tambah Data", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    String nama = namaField.getText();
                    String kapasitas = kapasitasField.getText();
                    String status = statusField.getText();
                    String alamat = alamatField.getText();

                    if (!nama.isEmpty() && !kapasitas.isEmpty() && !status.isEmpty() && !alamat.isEmpty()) {
                        try (Connection connection = DriverManager.getConnection(
                                "jdbc:mysql://localhost:3306/tubes_db", "root", "")) {
                            
                            String getIdSql = "SELECT MAX(id) AS last_id FROM drop_box";
                            int newId = 1; 
                            try (Statement stmt = connection.createStatement();
                                 ResultSet rs = stmt.executeQuery(getIdSql)) {
                                if (rs.next()) {
                                    newId = rs.getInt("last_id") + 1; 
                                }
                            }

                            String sql = "INSERT INTO drop_box (id, nama_dropbox, kapasitas, status, alamat) VALUES (?, ?, ?, ?, ?)";
                            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                                preparedStatement.setInt(1, newId);
                                preparedStatement.setString(2, nama);
                                preparedStatement.setString(3, kapasitas);
                                preparedStatement.setString(4, status);
                                preparedStatement.setString(5, alamat);
                                preparedStatement.executeUpdate();

                                tableModel.addRow(new Object[]{newId, nama, kapasitas, status, alamat});

                                JOptionPane.showMessageDialog(frame, "Data berhasil disimpan ke database!");
                            }
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(frame, "Gagal menyimpan data ke database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Semua field harus diisi.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Fitur ubah data belum diimplementasikan");
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow(); 
                if (selectedRow != -1) { 
                    int confirm = JOptionPane.showConfirmDialog(frame, "Apakah Anda yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        String id = tableModel.getValueAt(selectedRow, 0).toString();

                        try (Connection connection = DriverManager.getConnection(
                                "jdbc:mysql://localhost:3306/tubes_db", "root", "")) {
                            String sql = "DELETE FROM drop_box WHERE id = ?";
                            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                                preparedStatement.setString(1, id);
                                preparedStatement.executeUpdate();

                                tableModel.removeRow(selectedRow);

                                JOptionPane.showMessageDialog(frame, "Data berhasil dihapus!");
                            }
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(frame, "Gagal menghapus data dari database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Pilih baris yang ingin dihapus.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private static void loadTableData(DefaultTableModel tableModel) {
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/tubes_db", "root", "")) {
            String sql = "SELECT * FROM drop_box";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                while (resultSet.next()) {
                    String id = resultSet.getString("id");
                    String nama = resultSet.getString("nama_dropbox");
                    String kapasitas = resultSet.getString("kapasitas");
                    String status = resultSet.getString("status");
                    String alamat = resultSet.getString("alamat");
                    tableModel.addRow(new Object[]{id, nama, kapasitas, status, alamat});
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Gagal memuat data dari database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}