package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class JenisSampahView {
    public static void open() {

        JFrame frame = new JFrame("Jenis Sampah Elektronik");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel mainPanel = new JPanel(new BorderLayout());

        String[] columnNames = {"ID", "Jenis Sampah", "Total Berat (kg)"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
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

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField jenisField = new JTextField();
                JTextField beratField = new JTextField();

                Object[] inputFields = {
                    "Jenis Sampah:", jenisField,
                    "Total Berat (kg):", beratField
                };

                int option = JOptionPane.showConfirmDialog(frame, inputFields, "Tambah Data", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    String jenis = jenisField.getText();
                    String berat = beratField.getText();

                    if (!jenis.isEmpty() && !berat.isEmpty()) {
                        // Simpan data ke database
                        try (Connection connection = DriverManager.getConnection(
                                "jdbc:mysql://localhost:3306/tubes_db", "root", "")) {
                            String sql = "INSERT INTO jenis_sampah (nama_jenis_sampah, total_berat) VALUES (?, ?)";
                            try (PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                                preparedStatement.setString(1, jenis);
                                preparedStatement.setString(2, berat);
                                preparedStatement.executeUpdate();

                                // Ambil ID yang dihasilkan otomatis oleh database
                                try (var generatedKeys = preparedStatement.getGeneratedKeys()) {
                                    if (generatedKeys.next()) {
                                        int generatedId = generatedKeys.getInt(1);
                                        tableModel.addRow(new Object[]{generatedId, jenis, berat});
                                    }
                                }

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
                JOptionPane.showMessageDialog(frame, ".");
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow(); // Ambil baris yang dipilih
                if (selectedRow != -1) { // Periksa apakah ada baris yang dipilih
                    int confirm = JOptionPane.showConfirmDialog(frame, "Apakah Anda yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        // Ambil ID dari tabel
                        int id = (int) tableModel.getValueAt(selectedRow, 0);
        
                        // Hapus data dari database
                        try (Connection connection = DriverManager.getConnection(
                                "jdbc:mysql://localhost:3306/tubes_db", "root", "")) {
                            String sql = "DELETE FROM jenis_sampah WHERE id = ?";
                            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                                preparedStatement.setInt(1, id);
                                preparedStatement.executeUpdate();
        
                                // Hapus data dari tabel GUI
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
}