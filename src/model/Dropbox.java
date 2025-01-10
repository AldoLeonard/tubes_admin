package model;

public class Dropbox {
    private int id;
    private String namaDropbox;
    private String kapasitas;
    private String status;
    private String alamat;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setNamaDropbox(String namaDropbox) {
        this.namaDropbox = namaDropbox;
    }

    public String getNamaDropbox() {
        return namaDropbox;
    }
    
    public void setKapasitas(String kapasitas) {
        this.kapasitas = kapasitas;
    }

    public String getKapasitas() {
        return kapasitas;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
    
    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getAlamat() {
        return alamat;
    }

}
