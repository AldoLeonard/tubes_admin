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
                    rs.getInt("id"),
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

        // Menambahkan grid dan pengaturan lainnya untuk tampilan tabel
        table.setShowGrid(true);
        table.setGridColor(Color.BLACK);
        table.setDefaultEditor(Object.class, null);

        // Mengatur lebar kolom
        table.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
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
            @SuppressWarnings("CallToPrintStackTrace")
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
                JOptionPane.showMessageDialog(frame, "Fitur ubah data belum diimplementasikan");
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Fitur hapus data belum diimplementasikan");
            }
        });

        frame.add(mainPanel);

        frame.setVisible(true);
    }
}
