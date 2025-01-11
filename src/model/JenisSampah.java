package model;

public class JenisSampah {
    private int id;
    private String namaJenis;
    private String totalBerat;

    // Constructor
    public JenisSampah(int id, String namaJenis, String totalBerat) {
        this.id = id;
        this.namaJenis = namaJenis;
        this.totalBerat = totalBerat;
    }

    // Getter dan Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamaJenis() {
        return namaJenis;
    }

    public void setNamaJenis(String namaJenis) {
        this.namaJenis = namaJenis;
    }

    public String getTotalBerat() {
        return totalBerat;
    }

    public void setTotalBerat(String totalBerat) {
        this.totalBerat = totalBerat;
    }
}
