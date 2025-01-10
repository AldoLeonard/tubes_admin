package model;

public class JenisSampah {
    private int id;
    private String namaJenisSampah;
    private String deskripsi;
    private String totalBerat;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setNamaJenisSampah(String namaJenisSampah) {
        this.namaJenisSampah = namaJenisSampah;
    }

    public String getNamaJenisSampah() {
        return namaJenisSampah;
    }
    
    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getDeskripsi() {
        return deskripsi;
    }
    public void setTotalBerat(String totalBerat) {
        this.totalBerat = totalBerat;
    }

    public String getTotalBerat() {
        return totalBerat;
    }

}
