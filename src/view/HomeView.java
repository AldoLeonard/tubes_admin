// File: src/view/HomeView.java
package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


public class HomeView extends JFrame{
    @SuppressWarnings("Convert2Lambda")
    public static void main(String[] args) {

        JFrame frame = new JFrame("Bank Sampah");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(51, 204, 255));

        JPanel menuPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        menuPanel.setBackground(Color.WHITE);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton jenisSampahButton = new JButton("Jenis Sampah Elektronik");
        JButton approvalButton = new JButton("Approval Registrasi");
        JButton dropboxButton = new JButton("Dropbox");
        JButton konversiPoinButton = new JButton("Konversi Poin");

        jenisSampahButton.addActionListener(new ActionListener() {
        @Override
            public void actionPerformed(ActionEvent e) {
            JenisSampahView.open();
            }
        });

        approvalButton.addActionListener(new ActionListener() {
        @Override
            public void actionPerformed(ActionEvent e) {
            ApprovalRegistrasiView.open();
            }
        });

        dropboxButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DropboxView.open();
            }
        });

        konversiPoinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                KonversiPoinView.open();
            }
        });

        menuPanel.add(jenisSampahButton);
        menuPanel.add(approvalButton);
        menuPanel.add(dropboxButton);
        menuPanel.add(konversiPoinButton);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(menuPanel, BorderLayout.CENTER);

        frame.add(mainPanel);


        frame.setVisible(true);
    }
}