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

public class DropboxView {
    public static void open() {
        JFrame frame = new JFrame("Dropbox Management");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel mainPanel = new JPanel(new BorderLayout());

        String[] columnNames = {"ID", "Nama Dropbox", "Kapasitas", "Status", "Alamat Dropbox"};
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
        JTextField idField = new JTextField();
        JTextField namaField = new JTextField();
        JTextField kapasitasField = new JTextField();
        JTextField statusField = new JTextField();
        JTextField alamatField = new JTextField();

        Object[] inputFields = {
            "ID:", idField,
            "Nama Dropbox:", namaField,
            "Kapasitas:", kapasitasField,
            "Status:", statusField,
            "Alamat Dropbox:", alamatField
        };

        int option = JOptionPane.showConfirmDialog(frame, inputFields, "Tambah Data", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String id = idField.getText();
            String nama = namaField.getText();
            String kapasitas = kapasitasField.getText();
            String status = statusField.getText();
            String alamat = alamatField.getText();

            if (!id.isEmpty() && !nama.isEmpty() && !kapasitas.isEmpty() && !status.isEmpty() && !alamat.isEmpty()) {
                // Tambahkan data ke tabel
                tableModel.addRow(new Object[]{id, nama, kapasitas, status, alamat});

                // Simpan data ke database
                try (Connection connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/tubes_db", "root", "")) {
                    String sql = "INSERT INTO drop_box (id, nama_dropbox, kapasitas, status, alamat) VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                        preparedStatement.setString(1, id);
                        preparedStatement.setString(2, nama);
                        preparedStatement.setString(3, kapasitas);
                        preparedStatement.setString(4, status);
                        preparedStatement.setString(5, alamat);
                        preparedStatement.executeUpdate();
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
                JOptionPane.showMessageDialog(frame, ".");
            }
        });

        frame.add(mainPanel);

        frame.setVisible(true);
    }
}