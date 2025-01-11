package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class KonversiPoinView {
    @SuppressWarnings("CallToPrintStackTrace")
    private static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tubes_db", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database connection failed!");
        }
        return conn;
    }

    @SuppressWarnings("CallToPrintStackTrace")
    private static void loadData(DefaultTableModel tableModel) {
        // Hapus semua baris sebelumnya di tabel
        tableModel.setRowCount(0);

        String query = "SELECT * FROM konversi_poin";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"), // Tetap memuat ID untuk keperluan manipulasi data
                    rs.getString("nama_lengkap"),
                    rs.getInt("poin_sampah")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal memuat data!");
        }
    }

    @SuppressWarnings("Convert2Lambda")
    public static void open() {
        JFrame frame = new JFrame("Konversi Poin");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Sesuaikan dengan kolom
        String[] columnNames = {"ID", "Nama Lengkap", "Poin Sampah"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);

        // Sembunyikan kolom ID
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);

        // Menambahkan grid dan pengaturan lainnya untuk tampilan tabel
        table.setShowGrid(true);
        table.setGridColor(Color.BLACK);
        table.setDefaultEditor(Object.class, null);

        // Mengatur lebar kolom lainnya
        table.getColumnModel().getColumn(1).setPreferredWidth(150); // Nama Lengkap
        table.getColumnModel().getColumn(2).setPreferredWidth(100); // Poin Sampah

        // Menambahkan JScrollPane untuk tabel
        JScrollPane tableScrollPane = new JScrollPane(table);
        table.setPreferredScrollableViewportSize(new Dimension(750, 400));
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Tambah Data");
        JButton updateButton = new JButton("Ubah Data");
        JButton deleteButton = new JButton("Hapus Data");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Panggil loadData di sini untuk memuat data saat aplikasi dibuka
        loadData(tableModel);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField namaField = new JTextField(10);
                JTextField poinField = new JTextField(10);

                JPanel inputPanel = new JPanel(new GridLayout(2, 2));
                inputPanel.add(new JLabel("Nama Lengkap:"));
                inputPanel.add(namaField);
                inputPanel.add(new JLabel("Poin Sampah:"));
                inputPanel.add(poinField);

                int result = JOptionPane.showConfirmDialog(frame, inputPanel, "Tambah Data", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    String nama = namaField.getText();
                    String poinSampah = poinField.getText();

                    try (Connection conn = connect();
                         PreparedStatement ps = conn.prepareStatement("INSERT INTO konversi_poin (nama_lengkap, poin_sampah) VALUES (?, ?)");) {
                        ps.setString(1, nama);
                        ps.setString(2, poinSampah);
                        ps.executeUpdate();
                        JOptionPane.showMessageDialog(frame, "Data berhasil ditambahkan!");

                        // Panggil loadData untuk memperbarui tabel setelah menambahkan data
                        loadData(tableModel);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Gagal menambahkan data!");
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
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    String namaAwal = (String) tableModel.getValueAt(selectedRow, 1);
                    int poinAwal = (int) tableModel.getValueAt(selectedRow, 2);

                    // Buat form input
                    JTextField namaField = new JTextField(namaAwal, 10);
                    JTextField poinField = new JTextField(String.valueOf(poinAwal), 10);

                    JPanel inputPanel = new JPanel(new GridLayout(2, 2));
                    inputPanel.add(new JLabel("Nama Lengkap:"));
                    inputPanel.add(namaField);
                    inputPanel.add(new JLabel("Poin Sampah:"));
                    inputPanel.add(poinField);

                    int result = JOptionPane.showConfirmDialog(frame, inputPanel, "Ubah Data", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        String namaBaru = namaField.getText();
                        String poinBaruStr = poinField.getText();

                        try {
                            int poinBaru = Integer.parseInt(poinBaruStr); // Validasi input poin sebagai angka
                            // Update data di database
                            try (Connection conn = connect();
                                 PreparedStatement ps = conn.prepareStatement("UPDATE konversi_poin SET nama_lengkap = ?, poin_sampah = ? WHERE id = ?")) {
                                ps.setString(1, namaBaru);
                                ps.setInt(2, poinBaru);
                                ps.setInt(3, id);
                                ps.executeUpdate();
                                JOptionPane.showMessageDialog(frame, "Data berhasil diubah!");

                                // Memperbarui data di tabel GUI
                                loadData(tableModel);
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(frame, "Gagal mengubah data!");
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(frame, "Poin harus berupa angka.", "Error", JOptionPane.ERROR_MESSAGE);
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
                int selectedRow = table.getSelectedRow(); // Ambil baris yang dipilih
                if (selectedRow != -1) { // Periksa apakah ada baris yang dipilih
                    int confirm = JOptionPane.showConfirmDialog(frame, "Apakah Anda yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        // Ambil ID dari tabel
                        int id = (int) tableModel.getValueAt(selectedRow, 0);

                        // Hapus data dari database
                        try (Connection conn = connect();
                             PreparedStatement ps = conn.prepareStatement("DELETE FROM konversi_poin WHERE id = ?")) {
                            ps.setInt(1, id);
                            ps.executeUpdate();

                            // Perbarui tabel
                            JOptionPane.showMessageDialog(frame, "Data berhasil dihapus!");
                            loadData(tableModel); // Memperbarui data di tabel GUI
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(frame, "Gagal menghapus data dari database!");
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
