package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class ApprovalRegistrasiView extends JFrame {
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
        JTextField txtNama = new JTextField();
        txtNama.setBounds(130, 20, 180, 25);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(20, 60, 100, 25);
        JTextField txtEmail = new JTextField();
        txtEmail.setBounds(130, 60, 180, 25);

        JLabel lblAlamat = new JLabel("Alamat Rumah:");
        lblAlamat.setBounds(20, 100, 100, 25);
        JTextField txtAlamat = new JTextField();
        txtAlamat.setBounds(130, 100, 180, 25);

        JLabel lblTelepon = new JLabel("No Telepon:");
        lblTelepon.setBounds(20, 140, 100, 25);
        JTextField txtTelepon = new JTextField();
        txtTelepon.setBounds(130, 140, 180, 25);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(20, 180, 100, 25);
        JTextField txtPassword = new JTextField();
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

        btnApprove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Penyimpanan data berhasil", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnReject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Data ditolak", "Informasi", JOptionPane.WARNING_MESSAGE);
            }
        });

        setVisible(true);
    }

    public static void open() {
        new ApprovalRegistrasiView();
    }
}
