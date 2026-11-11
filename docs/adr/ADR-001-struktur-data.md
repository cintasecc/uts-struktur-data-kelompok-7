# ADR-001 — Struktur data utama
Status: Accepted

Penyimpanan utama runtime menggunakan `SinglyLinkedList<Putusan>` buatan sendiri untuk memenuhi requirement mata kuliah Struktur Data. Stack custom menyimpan aktivitas audit. ArrayList hanya digunakan sebagai objek transfer hasil pembacaan/ekspor dan bukan sebagai penyimpanan utama.
