package view;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.*;

public class ApprovalRegistrasiView extends JFrame {
    private JTextField txtNama;
    private JTextField txtEmail;
    private JTextField txtAlamat;
    private JTextField txtTelepon;
    private JTextField txtPassword;

    public ApprovalRegistrasiView() {
        setTitle("Approval Registrasi");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(null);
        formPanel.setBounds(20, 20, 340, 300);

        JLabel lblNama = new JLabel("Nama Lengkap:");
        lblNama.setBounds(20, 20, 100, 25);
        txtNama = new JTextField();
        txtNama.setBounds(130, 20, 180, 25);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(20, 60, 100, 25);
        txtEmail = new JTextField();
        txtEmail.setBounds(130, 60, 180, 25);

        JLabel lblAlamat = new JLabel("Alamat Rumah:");
        lblAlamat.setBounds(20, 100, 100, 25);
        txtAlamat = new JTextField();
        txtAlamat.setBounds(130, 100, 180, 25);

        JLabel lblTelepon = new JLabel("No Telepon:");
        lblTelepon.setBounds(20, 140, 100, 25);
        txtTelepon = new JTextField();
        txtTelepon.setBounds(130, 140, 180, 25);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(20, 180, 100, 25);
        txtPassword = new JTextField();
        txtPassword.setBounds(130, 180, 180, 25);

        JButton btnApprove = new JButton("Approve");
        btnApprove.setBounds(50, 240, 100, 30);
        JButton btnReject = new JButton("Reject");
        btnReject.setBounds(180, 240, 100, 30);

        formPanel.add(lblNama);
        formPanel.add(txtNama);
        formPanel.add(lblEmail);
        formPanel.add(txtEmail);
        formPanel.add(lblAlamat);
        formPanel.add(txtAlamat);
        formPanel.add(lblTelepon);
        formPanel.add(txtTelepon);
        formPanel.add(lblPassword);
        formPanel.add(txtPassword);
        formPanel.add(btnApprove);
        formPanel.add(btnReject);

        add(formPanel);

        // ActionListener untuk tombol Approve
        btnApprove.addActionListener(e -> {
            String nama = txtNama.getText();
            String email = txtEmail.getText();
            String alamat = txtAlamat.getText();
            String telepon = txtTelepon.getText();
            String password = txtPassword.getText();

            if (telepon.length() > 15) {
                JOptionPane.showMessageDialog(null, "Nomor telepon terlalu panjang. Maksimal 15 karakter.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!nama.isEmpty() && !email.isEmpty() && !alamat.isEmpty() && !telepon.isEmpty() && !password.isEmpty()) {
                // Simpan data ke database
                try (Connection connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/tubes_db", "root", "")) {

                    String sql = "INSERT INTO users (nama, email, alamat, no_telepon, password) VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                        preparedStatement.setString(1, nama);
                        preparedStatement.setString(2, email);
                        preparedStatement.setString(3, alamat);
                        preparedStatement.setString(4, telepon);
                        preparedStatement.setString(5, password);
                        preparedStatement.executeUpdate();

                        JOptionPane.showMessageDialog(null, "Data berhasil disimpan ke database", "Informasi", JOptionPane.INFORMATION_MESSAGE);
                        clearFields(); // Kosongkan field setelah data berhasil disimpan

                        //  // Pindah ke halaman HomeView
                        // new HomeView(); 
                        // dispose(); // Tutup ApprovalRegistrasiView

                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Gagal menyimpan data ke database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Semua field harus diisi.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // ActionListener untuk tombol Reject
        btnReject.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Data ditolak", "Informasi", JOptionPane.WARNING_MESSAGE);
            clearFields(); // Kosongkan field setelah data ditolak
        });


        setVisible(true);
    }

    public void setData(String nama, String email, String alamat, String telepon, String password) {
        txtNama.setText(nama);
        txtEmail.setText(email);
        txtAlamat.setText(alamat);
        txtTelepon.setText(telepon);
        txtPassword.setText(password);
    }

    private void clearFields() {
        txtNama.setText("");
        txtEmail.setText("");
        txtAlamat.setText("");
        txtTelepon.setText("");
        txtPassword.setText("");
    }

    public static void open() {
        new ApprovalRegistrasiView();
    }
}
