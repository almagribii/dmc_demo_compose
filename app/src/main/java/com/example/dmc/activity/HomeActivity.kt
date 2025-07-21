// src/main/java/com/example/dmc/activity/HomeActivity.kt
package com.example.dmc.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dmc.R
import com.example.dmc.ui.theme.DMCTheme

/**
 * HomeActivity adalah Activity utama setelah pengguna berhasil login.
 * Ini bertanggung jawab untuk menerima data pengguna dari LoginActivity
 * dan menampilkannya di HomeScreen Composable, serta mengelola navigasi
 * ke fitur-fitur aplikasi lainnya.
 */
class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ambil data pengguna dari Intent yang dikirim oleh LoginActivity
        val userName = intent.getStringExtra("USER_NAME") ?: "Pasien"
        val userNim = intent.getStringExtra("USER_NIM") ?: ""
        val pasienId = intent.getLongExtra("PASIEN_ID", -1L) // ID Pasien, default -1L jika tidak ada

        // Debugging: Cetak nilai pasienId yang diterima HomeActivity
        // Ini sangat penting untuk melacak masalah "ID Pasien tidak ditemukan"
        println("DEBUG_ID: HomeActivity menerima pasienId: $pasienId")

        setContent {
            DMCTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    val applicationContext = context.applicationContext // Dapatkan applicationContext yang stabil

                    HomeScreen(
                        userName = userName,
                        onLogoutClick = {
                            // Logika Logout: Tampilkan Toast, arahkan ke LoginActivity, dan hapus back stack
                            Toast.makeText(applicationContext, "Anda telah Logout", Toast.LENGTH_SHORT).show()
                            val intent = Intent(context, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Bersihkan semua Activity sebelumnya
                            context.startActivity(intent)
                        },
                        onViewMedicalRecordsClick = {
                            // Navigasi ke MedicalRecordsListActivity untuk melihat jejak rekam medis
                            Toast.makeText(applicationContext, "Membuka Jejak Rekam Medis...", Toast.LENGTH_SHORT).show()
                            // Debugging: Cetak nilai pasienId sebelum dikirim
                            println("DEBUG_ID: HomeActivity mengirim pasienId ke MedicalRecordsListActivity: $pasienId")
                            val intent = Intent(context, MedicalRecordsListActivity::class.java).apply {
                                putExtra("PASIEN_ID", pasienId) // Teruskan ID Pasien ke layar daftar rekam medis
                            }
                            context.startActivity(intent)
                        },
                        onAddMedicalRecordClick = { // Callback baru untuk menambah rekam medis
                            // Navigasi ke AddMedicalRecordActivity untuk menambah rekam medis
                            Toast.makeText(applicationContext, "Membuka Form Tambah Rekam Medis...", Toast.LENGTH_SHORT).show()
                            // Debugging: Cetak nilai pasienId sebelum dikirim
                            println("DEBUG_ID: HomeActivity mengirim pasienId ke AddMedicalRecordActivity: $pasienId")
                            val intent = Intent(context, AddMedicalRecordActivity::class.java).apply {
                                putExtra("PASIEN_ID", pasienId) // Teruskan ID Pasien ke layar tambah rekam medis
                            }
                            context.startActivity(intent)
                        },
                        onAppointmentClick = {
                            // Navigasi ke fitur Buat Janji Temu
                            Toast.makeText(applicationContext, "Membuka Buat Janji Temu...", Toast.LENGTH_SHORT).show()
                            // TODO: Implementasi Intent untuk Buat Janji Temu Activity
                        },
                        onMedicineInfoClick = {
                            // Navigasi ke fitur Informasi Obat
                            Toast.makeText(applicationContext, "Membuka Informasi Obat...", Toast.LENGTH_SHORT).show()
                            val intent = Intent(context, MedicineInfoActivity::class.java)
                            context.startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}

/**
 * HomeScreen adalah Composable yang menampilkan UI utama setelah login.
 * Ini menerima data pengguna dan callbacks untuk berbagai aksi.
 * Menggunakan Scaffold untuk struktur dasar UI Material Design.
 */
@OptIn(ExperimentalMaterial3Api::class) // Diperlukan untuk TopAppBar
@Composable
fun HomeScreen(
    userName: String = "Pasien", // Nama pengguna yang login
    onLogoutClick: () -> Unit = {}, // Callback untuk aksi logout
    onViewMedicalRecordsClick: () -> Unit = {}, // Callback untuk melihat rekam medis
    onAddMedicalRecordClick: () -> Unit = {}, // Callback baru untuk menambah rekam medis
    onAppointmentClick: () -> Unit = {}, // Callback untuk buat janji temu
    onMedicineInfoClick: () -> Unit = {}, // Callback untuk informasi obat
) {
    val context = LocalContext.current // Context dari Composable

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Selamat Datang, ${userName.split(" ").firstOrNull() ?: "Pasien"}!", // Ambil nama depan
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                actions = {
                    // Ikon Aksi di kanan atas AppBar
                    IconButton(onClick = {
                        // Tampilkan Toast menggunakan context dari Composable (aman di sini karena immediate)
                        Toast.makeText(context, "Informasi Aplikasi", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(Icons.Filled.Info, contentDescription = "Informasi Aplikasi")
                    }
                    IconButton(onClick = onLogoutClick) { // Tombol Logout sebagai ikon
                        Icon(Icons.Filled.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        }
    ) { paddingValues -> // paddingValues dari Scaffold untuk menghindari konten tertutup TopAppBar
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Terapkan padding dari Scaffold
        ) {
            // Box untuk menampung Card sambutan dengan gambar dan teks
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp) // Padding untuk Box
            ) {
                Card(
                    modifier = Modifier.fillMaxSize(), // Card mengisi Box
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer), // Warna latar belakang Card
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Elevasi Card
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically // Pusatkan konten Row secara vertikal
                    ) {
                        Image(
                            painter = painterResource(R.drawable.person), // Pastikan R.drawable.person ada di res/drawable
                            contentDescription = "Ilustrasi Sapaan",
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(120.dp)
                                .offset(y = 20.dp), // Geser gambar sedikit ke bawah
                            alignment = Alignment.BottomStart, // Gambar sejajar di kiri bawah
                            contentScale = ContentScale.Fit // Skala gambar
                        )
                        Text(
                            text = "Selamat Pagi! Bagaimana Kabar Anda?", // Pesan sapaan
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier
                                .weight(1f) // Text mengambil sisa ruang horizontal
                                .padding(start = 8.dp) // Padding dari gambar
                        )
                    }
                }
            }

            // Spacer setelah Card untuk memberi jarak ke tombol fitur
            Spacer(modifier = Modifier.height(24.dp))

            // Kolom untuk menampung tombol-tombol fitur aplikasi
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp), // Beri padding horizontal di sini
                horizontalAlignment = Alignment.CenterHorizontally, // Pusatkan tombol secara horizontal
                verticalArrangement = Arrangement.spacedBy(8.dp) // Beri jarak vertikal antar tombol
            ) {
                // Tombol untuk Melihat Rekam Medis (Daftar)
                Button(
                    onClick = onViewMedicalRecordsClick, // Panggil callback melihat rekam medis
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Lihat Jejak Rekam Medis", fontSize = 18.sp)
                }

                // Tombol untuk Menambah Rekam Medis Baru (Form)
                Button(
                    onClick = onAddMedicalRecordClick, // Panggil callback menambah rekam medis
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer) // Warna berbeda
                ) {
                    Text("Tambah Rekam Medis Baru", fontSize = 18.sp)
                }

                // Tombol untuk Buat Janji Temu
                Button(
                    onClick = onAppointmentClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer) // Warna berbeda
                ) {
                    Text("Buat Janji Temu", fontSize = 18.sp)
                }

                // Tombol untuk Informasi Obat
                Button(
                    onClick = onMedicineInfoClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer) // Warna berbeda
                ) {
                    Text("Informasi Obat", fontSize = 18.sp)
                }
            }

            Spacer(modifier = Modifier.height(64.dp)) // Spacer di bagian bawah
        }
    }
}

/**
 * Preview untuk HomeScreen Composable.
 * Memungkinkan Anda melihat tampilan UI di Android Studio tanpa perlu menjalankan aplikasi di emulator.
 */
@Preview(showBackground = true, widthDp = 360, heightDp = 720)
@Composable
fun PreviewHomeScreenWithAppBar() {
    DMCTheme {
        HomeScreen(userName = "Budi Santoso")
    }
}