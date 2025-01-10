package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class JenisSampahView {

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
        
        String query = "SELECT * FROM jenis_sampah";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("nama_jenis_sampah"),
                    rs.getString("total_berat")
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

        JFrame frame = new JFrame("Jenis Sampah Elektronik");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Nama kolom
        String[] columnNames = {"ID", "Jenis Sampah", "Total Berat (kg)"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        
        // Membuat tabel dengan model yang telah ditentukan
        JTable table = new JTable(tableModel) {
            // Membuat tabel tidak dapat diedit
            @SuppressWarnings("override")
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Menambahkan gridlines pada tabel agar lebih rapi
        table.setShowGrid(true);
        table.setGridColor(Color.BLACK);
        
        // Agar lebar kolom lebih sesuai
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        
        // Menambahkan scrollbar pada tabel
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

        // Panggil loadData di sini untuk memuat data saat aplikasi dibuka
        loadData(tableModel);

        // Fungsi untuk menambahkan data
        addButton.addActionListener(new ActionListener() {
            @Override
            @SuppressWarnings("CallToPrintStackTrace")
            public void actionPerformed(ActionEvent e) {
                // Membuat input form untuk menambahkan data
                JTextField jenisSampahField = new JTextField(10);
                JTextField totalBeratField = new JTextField(10);

                JPanel inputPanel = new JPanel(new GridLayout(2, 2));
                inputPanel.add(new JLabel("Jenis Sampah:"));
                inputPanel.add(jenisSampahField);
                inputPanel.add(new JLabel("Total Berat (kg):"));
                inputPanel.add(totalBeratField);

                int result = JOptionPane.showConfirmDialog(frame, inputPanel, "Tambah Data Jenis Sampah", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    String jenisSampah = jenisSampahField.getText();
                    String totalBerat = totalBeratField.getText();

                    // Validasi input
                    if (jenisSampah.isEmpty() || totalBerat.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Semua field harus diisi!");
                        return;
                    }

                    // Menambahkan data ke tabel di database
                    try (Connection conn = connect();
                         PreparedStatement ps = conn.prepareStatement("INSERT INTO jenis_sampah (nama_jenis_sampah, total_berat) VALUES (?, ?)")) {
                        ps.setString(1, jenisSampah);
                        ps.setString(2, totalBerat); // Menggunakan String untuk total_berat
                        ps.executeUpdate();

                        // Memuat ulang data ke dalam tabel
                        loadData(tableModel);

                        JOptionPane.showMessageDialog(frame, "Data berhasil ditambahkan!");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Gagal menambahkan data!");
                    }
                }
            }
        });

        // Fungsi untuk mengubah data (belum diimplementasikan)
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Fitur ubah data belum diimplementasikan");
            }
        });

        // Fungsi untuk menghapus data (belum diimplementasikan)
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Fitur hapus data belum diimplementasikan");
            }
        });

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        open();
    }
}
