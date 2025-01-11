package view;

import controller.DropboxController;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Dropbox;

public class DropboxView {
    public static void open() {
        JFrame frame = new JFrame("Dropbox Management");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);

        DropboxController dropboxController = new DropboxController();
        // Hapus kolom "ID" dari definisi tabel
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Nama Dropbox", "Kapasitas (Kg)", "Status", "Alamat Dropbox"}, 0);
        JTable table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);
        frame.add(tableScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Tambah Data");
        JButton updateButton = new JButton("Ubah Data");
        JButton deleteButton = new JButton("Hapus Data");
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Tetap gunakan ID untuk operasi di latar belakang
        try {
            List<Dropbox> dropboxes = dropboxController.getAllDropboxes();
            for (Dropbox dropbox : dropboxes) {
                tableModel.addRow(new Object[]{
                    dropbox.getNamaDropbox(),
                    dropbox.getKapasitas() + " Kg",
                    dropbox.getStatus(),
                    dropbox.getAlamat()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Gagal memuat data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        addButton.addActionListener((ActionEvent e) -> {
            JTextField namaField = new JTextField();
            JTextField kapasitasField = new JTextField();
            JComboBox<String> statusField = new JComboBox<>(new String[]{"Aktif", "Tidak Aktif"});
            JTextField alamatField = new JTextField();

            Object[] inputFields = {"Nama Dropbox:", namaField, "Kapasitas:", kapasitasField, "Status:", statusField, "Alamat:", alamatField};
            int option = JOptionPane.showConfirmDialog(frame, inputFields, "Tambah Data", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                try {
                    Dropbox newDropbox = new Dropbox(0, namaField.getText(), kapasitasField.getText(), (String) statusField.getSelectedItem(), alamatField.getText());
                    dropboxController.addDropbox(newDropbox);
                    tableModel.addRow(new Object[]{
                        newDropbox.getNamaDropbox(),
                        newDropbox.getKapasitas() + " Kg",
                        newDropbox.getStatus(),
                        newDropbox.getAlamat()
                    });
                    JOptionPane.showMessageDialog(frame, "Data berhasil ditambahkan!");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Gagal menambah data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        updateButton.addActionListener((ActionEvent e) -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(frame, "Pilih baris data yang ingin diubah.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Tetap gunakan ID dari backend untuk identifikasi
            try {
                List<Dropbox> dropboxes = dropboxController.getAllDropboxes();
                int id = dropboxes.get(selectedRow).getId(); // ID diambil dari daftar, bukan tabel

                JTextField namaField = new JTextField(tableModel.getValueAt(selectedRow, 0).toString());
                JTextField kapasitasField = new JTextField(tableModel.getValueAt(selectedRow, 1).toString().replace(" Kg", ""));
                JComboBox<String> statusField = new JComboBox<>(new String[]{"Aktif", "Tidak Aktif"});
                JTextField alamatField = new JTextField(tableModel.getValueAt(selectedRow, 3).toString());
                Object[] inputFields = {"Nama Dropbox:", namaField, "Kapasitas:", kapasitasField, "Status:", statusField, "Alamat:", alamatField};
                int option = JOptionPane.showConfirmDialog(frame, inputFields, "Ubah Data", JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    Dropbox updatedDropbox = new Dropbox(id, namaField.getText(), kapasitasField.getText(), (String) statusField.getSelectedItem(), alamatField.getText());
                    dropboxController.updateDropbox(updatedDropbox);
                    tableModel.setValueAt(updatedDropbox.getNamaDropbox(), selectedRow, 0);
                    tableModel.setValueAt(updatedDropbox.getKapasitas() + " Kg", selectedRow, 1);
                    tableModel.setValueAt(updatedDropbox.getStatus(), selectedRow, 2);
                    tableModel.setValueAt(updatedDropbox.getAlamat(), selectedRow, 3);
                    JOptionPane.showMessageDialog(frame, "Data berhasil diubah!");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Gagal mengubah data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener((ActionEvent e) -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(frame, "Pilih baris data yang ingin dihapus.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                List<Dropbox> dropboxes = dropboxController.getAllDropboxes();
                int id = dropboxes.get(selectedRow).getId(); // ID diambil dari daftar, bukan tabel
                int confirm = JOptionPane.showConfirmDialog(frame, "Yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    dropboxController.deleteDropbox(id);
                    tableModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(frame, "Data berhasil dihapus!");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Gagal menghapus data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setVisible(true);
    }
}
