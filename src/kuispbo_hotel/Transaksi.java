package kuispbo_hotel;

import java.io.Serializable;

public class Transaksi implements Serializable {
    private String idTamu;
    private String kodeKamar;
    private int jumlah;

    public Transaksi(String idTamu, String kodeKamar, int jumlah) {
        this.idTamu = idTamu;
        this.kodeKamar = kodeKamar;
        this.jumlah = jumlah;
    }

    public String getIdTamu() { 
        return idTamu; 
    }
    
    public String getKodeKamar() { 
        return kodeKamar; 
    }
    
    public int getJumlah() { 
        return jumlah; 
    }
}