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
                int selectedRow = table.getSelectedRow(); // Ambil baris yang dipilih
                if (selectedRow != -1) { // Periksa apakah ada baris yang dipilih
                    // Ambil data dari tabel
                    String id = tableModel.getValueAt(selectedRow, 0).toString();
                    String nama = tableModel.getValueAt(selectedRow, 1).toString();
                    String kapasitas = tableModel.getValueAt(selectedRow, 2).toString();
                    String status = tableModel.getValueAt(selectedRow, 3).toString();
                    String alamat = tableModel.getValueAt(selectedRow, 4).toString();
        
                    // Buat form dengan data awal
                    JTextField namaField = new JTextField(nama);
                    JTextField kapasitasField = new JTextField(kapasitas);
                    JTextField statusField = new JTextField(status);
                    JTextField alamatField = new JTextField(alamat);
        
                    Object[] inputFields = {
                        "Nama Dropbox:", namaField,
                        "Kapasitas:", kapasitasField,
                        "Status:", statusField,
                        "Alamat Dropbox:", alamatField
                    };
        
                    // Tampilkan form
                    int option = JOptionPane.showConfirmDialog(frame, inputFields, "Ubah Data", JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        String namaBaru = namaField.getText();
                        String kapasitasBaru = kapasitasField.getText();
                        String statusBaru = statusField.getText();
                        String alamatBaru = alamatField.getText();
        
                        if (!namaBaru.isEmpty() && !kapasitasBaru.isEmpty() && !statusBaru.isEmpty() && !alamatBaru.isEmpty()) {
                            // Update data di database
                            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tubes_db", "root", "")) {
                                String sql = "UPDATE drop_box SET nama_dropbox = ?, kapasitas = ?, status = ?, alamat = ? WHERE id = ?";
                                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                                    preparedStatement.setString(1, namaBaru);
                                    preparedStatement.setString(2, kapasitasBaru);
                                    preparedStatement.setString(3, statusBaru);
                                    preparedStatement.setString(4, alamatBaru);
                                    preparedStatement.setString(5, id);
                                    preparedStatement.executeUpdate();
        
                                    // Update data di tabel GUI
                                    tableModel.setValueAt(namaBaru, selectedRow, 1);
                                    tableModel.setValueAt(kapasitasBaru, selectedRow, 2);
                                    tableModel.setValueAt(statusBaru, selectedRow, 3);
                                    tableModel.setValueAt(alamatBaru, selectedRow, 4);
        
                                    JOptionPane.showMessageDialog(frame, "Data berhasil diubah!");
                                }
                            } catch (SQLException ex) {
                                JOptionPane.showMessageDialog(frame, "Gagal mengubah data di database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(frame, "Semua field harus diisi.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Pilih baris yang ingin diubah.", "Error", JOptionPane.ERROR_MESSAGE);
                }
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