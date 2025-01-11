package model;

public class Dropbox {
    private int id;
    private String namaDropbox;
    private String kapasitas;
    private String status;
    private String alamat;

    // Constructor
    public Dropbox(int id, String namaDropbox, String kapasitas, String status, String alamat) {
        this.id = id;
        this.namaDropbox = namaDropbox;
        this.kapasitas = kapasitas;
        this.status = status;
        this.alamat = alamat;
    }

    // Getter dan Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamaDropbox() {
        return namaDropbox;
    }

    public void setNamaDropbox(String namaDropbox) {
        this.namaDropbox = namaDropbox;
    }

    public String getKapasitas() {
        return kapasitas;
    }

    public void setKapasitas(String kapasitas) {
        this.kapasitas = kapasitas;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
}
