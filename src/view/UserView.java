package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class UserView extends JFrame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Bank Sampah - User");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);
        frame.setLayout(null);

        // Panel untuk header
        JPanel headerPanel = new JPanel();
        headerPanel.setBounds(0, 20, 400, 50);
        JLabel headerLabel = new JLabel("Halaman User - Form Registrasi", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(headerLabel);

        // Panel untuk form
        JPanel formPanel = new JPanel();
        formPanel.setLayout(null);
        formPanel.setBounds(20, 60, 340, 300);

        // Label dan TextField
        JLabel nameLabel = new JLabel("Nama Lengkap:");
        nameLabel.setBounds(20, 20, 100, 25);
        JTextField nameField = new JTextField();
        nameField.setBounds(130, 20, 180, 25);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(20, 60, 100, 25);
        JTextField emailField = new JTextField();
        emailField.setBounds(130, 60, 180, 25);

        JLabel alamatLabel = new JLabel("Alamat Rumah:");
        alamatLabel.setBounds(20, 100, 100, 25);
        JTextField alamatField = new JTextField();
        alamatField.setBounds(130, 100, 180, 25);

        JLabel phoneLabel = new JLabel("No Telepon:");
        phoneLabel.setBounds(20, 140, 100, 25);
        JTextField phoneField = new JTextField();
        phoneField.setBounds(130, 140, 180, 25);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(20, 180, 100, 25);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(130, 180, 180, 25);

        // Tombol Submit
        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(120, 220, 100, 30);

        // Tambahkan ActionListener untuk tombol Submit
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nama = nameField.getText();
                String email = emailField.getText();
                String alamat = alamatField.getText();
                String telepon = phoneField.getText();
                String password = new String(passwordField.getPassword());

                // Buka ApprovalRegistrasiView dan kirimkan data
                ApprovalRegistrasiView approvalView = new ApprovalRegistrasiView();
                approvalView.setData(nama, email, alamat, telepon, password);
            }
        });

        // Tambahkan komponen ke formPanel
        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(alamatLabel);
        formPanel.add(alamatField);
        formPanel.add(phoneLabel);
        formPanel.add(phoneField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(submitButton);

        // Tambahkan panel ke frame
        frame.add(headerPanel);
        frame.add(formPanel);

        frame.setVisible(true);
    }
}
