package kuispbo_hotel;

import java.io.Serializable;

public class Tamu implements Serializable {
    private String id;
    private String nama;

    public Tamu(String id, String nama) {
        this.id = id;
        this.nama = nama;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }
}