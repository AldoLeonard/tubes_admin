package view;

import controller.JenisSampahController;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.JenisSampah;

public class JenisSampahView {
    public static void open() {
        JenisSampahController controller = new JenisSampahController();
        JFrame frame = new JFrame("Manajemen Jenis Sampah");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Hapus kolom ID dari tabel (hanya tampilkan nama_jenis_sampah dan total_berat)
        String[] columnNames = {"Nama Jenis Sampah", "Total Berat (Kg)"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Tambah");
        JButton updateButton = new JButton("Ubah");
        JButton deleteButton = new JButton("Hapus");
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Load Data
        loadTableData(controller, tableModel);

        // Tambah
        addButton.addActionListener((ActionEvent e) -> {
            JTextField namaField = new JTextField();
            JTextField beratField = new JTextField();

            Object[] fields = {"Nama Jenis Sampah:", namaField, "Total Berat (Kg):", beratField};
            int option = JOptionPane.showConfirmDialog(frame, fields, "Tambah Data", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                try {
                    String namaJenis = namaField.getText();
                    double berat = Double.parseDouble(beratField.getText()); // Konversi ke angka

                    JenisSampah jenisSampah = new JenisSampah(0, namaJenis, String.valueOf(berat));
                    if (controller.addJenisSampah(jenisSampah)) {
                        JOptionPane.showMessageDialog(frame, "Data berhasil ditambah!");
                        loadTableData(controller, tableModel);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Gagal menambah data.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Total Berat harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Ubah
        updateButton.addActionListener((ActionEvent e) -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                List<JenisSampah> jenisSampahList = controller.getAllJenisSampah();
                int id = jenisSampahList.get(selectedRow).getId();

                JTextField namaField = new JTextField(tableModel.getValueAt(selectedRow, 0).toString());
                JTextField beratField = new JTextField(tableModel.getValueAt(selectedRow, 1).toString().replace(" Kg", ""));

                Object[] fields = {"Nama Jenis Sampah:", namaField, "Total Berat (hanya angka):", beratField};
                int option = JOptionPane.showConfirmDialog(frame, fields, "Ubah Data", JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    try {
                        String namaJenis = namaField.getText();
                        double berat = Double.parseDouble(beratField.getText()); // Konversi ke angka

                        JenisSampah jenisSampah = new JenisSampah(id, namaJenis, String.valueOf(berat));
                        if (controller.updateJenisSampah(jenisSampah)) {
                            JOptionPane.showMessageDialog(frame, "Data berhasil diubah!");
                            loadTableData(controller, tableModel);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Gagal mengubah data.");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Total Berat harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Pilih data yang ingin diubah.");
            }
        });

        // Hapus
        deleteButton.addActionListener((ActionEvent e) -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                List<JenisSampah> jenisSampahList = controller.getAllJenisSampah();
                int id = jenisSampahList.get(selectedRow).getId();

                int confirm = JOptionPane.showConfirmDialog(frame, "Yakin ingin menghapus?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (controller.deleteJenisSampah(id)) {
                        JOptionPane.showMessageDialog(frame, "Data berhasil dihapus!");
                        loadTableData(controller, tableModel);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Gagal menghapus data.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Pilih data yang ingin dihapus.");
            }
        });

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private static void loadTableData(JenisSampahController controller, DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        List<JenisSampah> jenisSampahList = controller.getAllJenisSampah();
        for (JenisSampah jenisSampah : jenisSampahList) {
            // Konversi berat menjadi angka tanpa bagian desimal jika tidak diperlukan
            double berat = Double.parseDouble(jenisSampah.getTotalBerat());
            String beratFormatted = (berat % 1 == 0) ? String.format("%.0f", berat) : String.format("%.1f", berat);
            tableModel.addRow(new Object[]{jenisSampah.getNamaJenis(), beratFormatted + " Kg"});
        }
    }
    
}
