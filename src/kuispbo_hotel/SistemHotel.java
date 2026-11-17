package kuispbo_hotel;

import java.io.*;
import java.util.*;

public class SistemHotel implements Serializable {
    private String namaHotel;
    private HashMap<String, Tamu> daftarTamu;
    private HashMap<String, Kamar> daftarKamar;
    private ArrayList<Transaksi> logTransaksi;

    public SistemHotel(String namaHotel) {
        this.namaHotel = namaHotel;
        this.daftarTamu = new HashMap<>();
        this.daftarKamar = new HashMap<>();
        this.logTransaksi = new ArrayList<>();
        System.out.println("Sistem Hotel \"" + namaHotel + "\" berhasil diinisialisasi.");
    }

    public void tambahTamu(String id, String nama) {
        daftarTamu.put(id, new Tamu(id, nama));
        System.out.println("Tamu ditambahkan: " + id + " - " + nama);
    }

    public void tambahKamar(String kode, String nama, int kuota) {
        daftarKamar.put(kode, new Kamar(kode, nama, kuota));
        System.out.println("Kamar ditambahkan: " + kode + " - " + nama + " (Kuota: " + kuota + ")");
    }

    public void jalankanTransaksiDariFile(String namaFile) throws Exception {
        File file = new File(namaFile);
        if (!file.exists()) {
            throw new FileNotFoundException("File " + namaFile + " tidak ditemukan!");
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String baris = reader.readLine();
        reader.close();

        if (baris == null || baris.trim().isEmpty()) {
            System.out.println("File kosong.");
            return;
        }

        String[] transaksiArr = baris.split("\\|");

        for (String item : transaksiArr) {
            item = item.trim();
            if (item.isEmpty()) continue;

            try {
                prosesSatuTransaksi(item);
            } catch (TransaksiException e) {
                System.out.println("[TRANSAKSI GAGAL] " + e.getMessage());
            }
        }
    }

    private void prosesSatuTransaksi(String data) throws TransaksiException {
        String[] bagian = data.split("/");

        String idTamu = null;
        String kodeKamar = null;
        int jumlah = 0;

        for (String b : bagian) {
            b = b.trim();
            if (b.startsWith("ID-") || b.startsWith("ID>")) {
                idTamu = b.substring(3);
            } else if (b.startsWith("KD-") || b.startsWith("KD>")) {
                kodeKamar = b.substring(3);
            } else if (b.startsWith("JML-") || b.startsWith("JML>")) {
                try {
                    jumlah = Integer.parseInt(b.substring(4));
                } catch (NumberFormatException e) {
                    throw new TransaksiException("Format jumlah tidak valid: " + b);
                }
            }
        }

        if (idTamu == null || kodeKamar == null || jumlah <= 0) {
            throw new TransaksiException("Data transaksi tidak lengkap: " + data);
        }

        if (!daftarTamu.containsKey(idTamu)) {
            throw new TransaksiException("Tamu dengan ID " + idTamu + " tidak ditemukan!");
        }

        if (!daftarKamar.containsKey(kodeKamar)) {
            throw new TransaksiException("Kamar dengan kode " + kodeKamar + " tidak ditemukan!");
        }

        Kamar kamar = daftarKamar.get(kodeKamar);
        kamar.kurangiKuota(jumlah);

        Transaksi t = new Transaksi(idTamu, kodeKamar, jumlah);
        logTransaksi.add(t);

        System.out.println("Transaksi berhasil: " + t.getIdTamu() + " | " + t.getKodeKamar() + " | " + t.getJumlah());
    }

    public void simpanLaporanAkhir() {
        System.out.println("\n--- MENYIMPAN LAPORAN AKHIR ---");

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("snapshot.obj"))) {
            oos.writeObject(this);
            System.out.println("Serialization berhasil -> snapshot.obj");
        } catch (IOException e) {
            System.out.println("Gagal serialisasi: " + e.getMessage());
        }

        try (FileWriter writer = new FileWriter("laporan.txt")) {
            writer.write("=== DAFTAR KUNCI (ID SAJA) ===\n");
            writer.write("Hotel: " + namaHotel + "\n");
            writer.write("------------------------------\n");

            Set<String> kunci = daftarTamu.keySet();
            for (String id : kunci) {
                writer.write(id + "\n");
            }

            writer.write("\n--- Kode Kamar ---\n");
            for (String kode : daftarKamar.keySet()) {
                writer.write(kode + "\n");
            }

            System.out.println("Laporan teks berhasil ditulis -> laporan.txt");
        } catch (IOException e) {
            System.out.println("Gagal menulis laporan: " + e.getMessage());
        }
    }
}