# PUTUSAN DASHBOARD

Aplikasi desktop Java 21 + Swing untuk mengelola dataset 50 putusan perkara narkotika PN Pekanbaru. Struktur data utama runtime adalah **Custom Singly Linked List** dan audit menggunakan **Custom Stack**.

## Menjalankan
1. Pasang JDK 21 dan Maven 3.9+.
2. Buka folder proyek di IntelliJ IDEA / Eclipse atau terminal.
3. Jalankan `mvn clean test`.
4. Jalankan aplikasi dengan `mvn exec:java`.
5. Pastikan working directory adalah root proyek agar `data/putusan-50.csv` ditemukan.

Di Windows, bisa gunakan `mvnw` jika wrapper tersedia; pada paket ini jalankan perintah Maven yang terpasang.

## Login demo
Layar login menerima username apa pun yang tidak kosong. Password demo belum diverifikasi dan role dipilih untuk demonstrasi UI. Ini **bukan autentikasi produksi**. Admin dapat menghapus, Operator dapat menambah, Viewer hanya melihat.

## Fitur
- Dashboard ringkasan, tabel data, tambah/hapus dasar.
- Pencarian caseId / nomor perkara.
- Filter jenis, berat, dan pasal terbukti.
- Statistik vonis, audit log Stack, ekspor CSV/TXT.
- Ekstraksi teks PDF dengan PDFBox melalui SwingWorker; PDF scan memerlukan OCR.
- CSV import mempertahankan field kosong sebagai null.

## Dataset & provenance
`data/putusan-50.csv` (50 baris data) dan `data/data-provenance.csv` (650 catatan) disalin dari file yang diberikan. Isi dataset utama tidak diubah. Catatan provenance masih berstatus sesuai file sumber; reviewer/extractor kosong tidak diisi dengan asumsi.

## Catatan batasan yang diketahui
- Form tambah dan operasi hapus terhubung; editor penuh untuk seluruh atribut belum disediakan.
- Import PDF saat ini menampilkan preview teks dan belum melakukan pemetaan otomatis field ke form konfirmasi serta penulisan provenance per field.
- Login/role adalah simulasi lokal tanpa registrasi multi-user dan penyimpanan kredensial.
- PDF extractor belum OCR.
- Edit data, filter rentang vonis pada GUI, dan ekspor provenance belum tersedia pada UI.
Fitur yang belum selesai tersebut dicatat secara terbuka agar tidak mengklaim kriteria yang belum dipenuhi.


## UI/UX redesign

Versi UI terbaru menggunakan layout dashboard dengan sidebar navigasi, header profil, kartu ringkasan, tabel data yang dapat dicari, panel filter, ringkasan statistik, serta tampilan import/export yang lebih konsisten. Tema visual dibangun menggunakan komponen Java Swing dan helper `UiStyle` tanpa mengubah penyimpanan utama custom Singly Linked List atau audit Stack.

Catatan: login masih berupa simulasi demo; password belum diverifikasi dan role dipilih saat masuk. Panel import PDF saat ini hanya mengekstrak text layer untuk pratinjau, belum memetakan hasil ekstraksi otomatis ke record putusan.
