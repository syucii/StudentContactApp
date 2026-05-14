# NAMA : Sucitasari Rahmadani
# NIM : F1D02310138

# Student Contact App adalah aplikasi Android yang memungkinkan pengguna untuk mengelola data mahasiswa secara lengkap. Seperti menambah, memperbarui dan menghapus data mahasiswa. Aplikasi ini dibangun menggunakan Kotlin dari android studio.

Berikut beberapa tampilan halaman dari aplikasi Student Contact 
1. Login Screen
   <img width="352" height="796" alt="image" src="https://github.com/user-attachments/assets/5edda699-b887-4cd5-801f-1c55d6aec855" />
3. Daftar Mahasiswa
   <img width="347" height="799" alt="image" src="https://github.com/user-attachments/assets/f6686a45-eb8e-4143-bb56-cb91ec80260f" />
5. Detail Mahasiswa
   <img width="339" height="789" alt="image" src="https://github.com/user-attachments/assets/eda0321b-63c9-4aea-956c-e945af0328f0" />
7. Form Tambah Mahasiswa
   <img width="345" height="518" alt="image" src="https://github.com/user-attachments/assets/dcf64d9d-873e-4546-9bc7-d7cf991f9a0a" />
# Metode Penyimpanan yang di gunakan
1. SharedPreferences
   SharedPreferences cocok untuk menyimpan data sederhana bertipe key value seperti status login, nama pengguna, dan preferensi aplikasi (dark mode, notifikasi, ukuran font). Ada pada file PrefManager.kt dan SettingsManager.kt
2. Internal Storage
   Internal Storage cocok untuk menyimpan data berupa file teks. Catatan mahasiswa disimpan sebagai file .txt di direktori private aplikasi, sehingga tidak bisa diakses oleh aplikasi lain dan otomatis terhapus saat aplikasi di-uninstall. Ada pada FileHelper.kt dan DetailActivity.kt
3. Room Database
   Room Database untuk data terstruktur yang memerlukan operasi CRUD lengkap.Dan terdapat dukungan coroutine sehingga operasi database tidak memblokir UI. Ada pada StudentEntity.kt, StudentDao.kt dan AppDatabase.kt

# Kendala yang di hadapi dan cara mengatasinya 
1. Room Database tidak langsung terhubung ke RecyclerView, jadi saya menambahkan StudentAdapter.kt yang bertugas menghubungkan data dari Room Database ke tampilan RecyclerView, termasuk menangani klik tombol Edit, Hapus, dan swipe gesture.
2. RecyclerView dan CardView tidak terdaftar di libs.versions.toml bawaan project, menyebabkan error saat build, jadi harus menambahkan entry recyclerview dan cardview secara manual ke libs.versions.toml dan app/build.gradle.kts
