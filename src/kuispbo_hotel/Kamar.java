package kuispbo_hotel;

import java.io.Serializable;

public class Kamar implements Serializable {
    private String kode;
    private String nama;
    private int kuota;

    public Kamar(String kode, String nama, int kuota) {
        this.kode = kode;
        this.nama = nama;
        this.kuota = kuota;
    }

    public String getKode() { 
        return kode; 
    }
    
    public int getKuota() { 
        return kuota; 
    }

    public void kurangiKuota(int jumlah) throws TransaksiException {
        if (jumlah > kuota) {
            throw new TransaksiException("Kuota kamar " + kode + " tidak mencukupi. Sisa: " + kuota);
        }
        this.kuota -= jumlah;
    }
}