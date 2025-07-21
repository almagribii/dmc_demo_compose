// src/main/java/com/example/dmc/activity/HomeActivity.kt
package com.example.dmc.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image // Tetap butuh ini karena kita menggunakan Icon dengan ImageVector
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalHospital // Tambah Rekam Medis
import androidx.compose.material.icons.filled.MedicalServices // Jejak Rekam Medis
import androidx.compose.material.icons.filled.Medication // Informasi Obat
import androidx.compose.material.icons.filled.HealthAndSafety // <-- ILUSTRASI CARD ATAS
import androidx.compose.material.icons.filled.MenuBook // <-- ILUSTRASI CARD BAWAH
import androidx.compose.material.icons.filled.CalendarMonth // Jika nanti digunakan untuk janji temu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource // Tetap butuh ini jika menggunakan R.drawable untuk Icon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dmc.R // Hanya jika ada resource lain seperti string/tema
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
                                putExtra("PASIEN_ID", pasienId) // Teruskan ID Pasien
                                putExtra("PATIENT_NAME", userName) // Teruskan NAMA PASIEN
                            }
                            context.startActivity(intent)
                        },
                        onAddMedicalRecordClick = {
                            // Navigasi ke AddMedicalRecordActivity untuk menambah rekam medis
                            Toast.makeText(applicationContext, "Membuka Form Tambah Rekam Medis...", Toast.LENGTH_SHORT).show()
                            // Debugging: Cetak nilai pasienId sebelum dikirim
                            println("DEBUG_ID: HomeActivity mengirim pasienId ke AddMedicalRecordActivity: $pasienId")
                            val intent = Intent(context, AddMedicalRecordActivity::class.java).apply {
                                putExtra("PASIEN_ID", pasienId) // Teruskan ID Pasien
                                putExtra("PATIENT_NAME", userName) // Teruskan NAMA PASIEN
                            }
                            context.startActivity(intent)
                        },
                        onDispenseMedicineClick = { // <-- NAMA CALLBACK DIUBAH
                            Toast.makeText(applicationContext, "Membuka Form Pemberian Obat...", Toast.LENGTH_SHORT).show()
                            val intent = Intent(context, DispenseMedicineActivity::class.java) // Intent ke Activity baru
                            context.startActivity(intent)
                        },
                        onAppointmentClick = { // Ini adalah Buat Janji Temu yang asli, jika ingin tetap ada
                            Toast.makeText(applicationContext, "Membuka Buat Janji Temu...", Toast.LENGTH_SHORT).show()
                            // TODO: Implementasi Intent untuk Buat Janji Temu Activity (misal AppointmentActivity)
                            // val intent = Intent(context, AppointmentActivity::class.java)
                            // context.startActivity(intent)
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
    onAddMedicalRecordClick: () -> Unit = {}, // Callback untuk menambah rekam medis
    onDispenseMedicineClick: () -> Unit = {}, // <-- CALLBACK UNTUK PEMBERIAN OBAT
    onAppointmentClick: () -> Unit = {}, // Callback untuk buat janji temu (jika masih ada)
    onMedicineInfoClick: () -> Unit = {}, // Callback untuk informasi obat
) {
    val context = LocalContext.current // Context dari Composable

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        // Menggunakan nama lengkap atau nama depan saja
                        text = "Selamat Datang, ${userName.split(" ").firstOrNull() ?: "Pasien"}!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                actions = {
                    // Ikon Informasi Aplikasi
                    IconButton(onClick = {
                        Toast.makeText(context, "Informasi Aplikasi", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(Icons.Filled.Info, contentDescription = "Informasi Aplikasi")
                    }
                    // Ikon Logout
                    IconButton(onClick = onLogoutClick) {
                        Icon(Icons.Filled.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        }
    ) { paddingValues -> // paddingValues dari Scaffold untuk menghindari konten tertutup TopAppBar
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Menerapkan padding dari Scaffold
                .background(MaterialTheme.colorScheme.background) // Latar belakang utama
        ) {
            // --- Bagian Card / Banner Atas ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp) // Sesuaikan tinggi banner
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(16.dp), // Sudut membulat
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer), // Warna latar belakang Card
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Elevasi Card
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Menggunakan Icon dari Icons.Filled untuk ilustrasi orang
                        Icon(
                            imageVector = Icons.Filled.HealthAndSafety, // Ikon ini cocok untuk ilustrasi kesehatan
                            contentDescription = "Health and Safety Icon",
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(150.dp),
                            tint = MaterialTheme.colorScheme.primary // Atur warna ikon
                        )
                        Text(
                            text = "Ayo Jaga Kesehatan Anda!", // Pesan sapaan baru
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier
                                .weight(1f) // Text mengambil sisa ruang
                                .padding(end = 8.dp), // Padding dari tepi kanan
                            textAlign = TextAlign.End // Rata kanan
                        )
                    }
                }
            }

            // --- Spacer setelah banner atas ---
            Spacer(modifier = Modifier.height(24.dp))

            // --- Bagian 4 Menu Tengah (Grid 2x2) ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp) // Jarak antar baris grid
            ) {
                // Baris 1 Menu
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround, // Jarak antar item di baris
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Menu 1: Lihat Jejak Rekam Medis
                    MenuItemCard(
                        icon = Icons.Filled.MedicalServices,
                        text = "Jejak Rekam Medis",
                        onClick = onViewMedicalRecordsClick,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(16.dp)) // Jarak antar Card
                    // Menu 2: Tambah Rekam Medis Baru
                    MenuItemCard(
                        icon = Icons.Filled.LocalHospital,
                        text = "Tambah Rekam Medis",
                        onClick = onAddMedicalRecordClick,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Baris 2 Menu
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Menu 3: Pemberian Obat (mengganti Buat Janji Temu)
                    MenuItemCard(
                        icon = Icons.Filled.MedicalServices, // <-- IKON BARU
                        text = "Pemberian Obat", // <-- TEKS BARU
                        onClick = onDispenseMedicineClick, // <-- CALLBACK BARU
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    // Menu 4: Informasi Obat
                    MenuItemCard(
                        icon = Icons.Filled.Medication,
                        text = "Informasi Obat",
                        onClick = onMedicineInfoClick,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // --- Spacer setelah menu grid ---
            Spacer(modifier = Modifier.height(32.dp))

            // --- Bagian Card / Banner Bawah ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp) // Sesuaikan tinggi banner
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Kesehatan itu Investasi,\nMari Jaga Sejak Dini!",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 16.dp, end = 8.dp)
                        )
                        Icon(
                            imageVector = Icons.Filled.MenuBook, // Ikon buku
                            contentDescription = "Book Icon",
                            modifier = Modifier
                                .size(90.dp)
                                .padding(end = 16.dp),
                            tint = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }
        }
    }
}

/**
 * Composable kustom untuk item menu (tombol) di HomeScreen.
 */
@Composable
fun RowScope.MenuItemCard( // Menggunakan RowScope agar bisa menggunakan .weight(1f)
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(130.dp) // Tinggi Card item menu
            .clip(RoundedCornerShape(12.dp)) // Sudut membulat
            .clickable(onClick = onClick), // Membuat Card bisa diklik
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), // Warna item menu
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                modifier = Modifier.size(48.dp), // Ukuran ikon
                tint = MaterialTheme.colorScheme.primary // Warna ikon
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
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