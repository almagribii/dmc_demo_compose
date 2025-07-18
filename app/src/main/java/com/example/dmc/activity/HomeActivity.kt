// src/main/java/com/example/dmc/activity/HomeScreen.kt
package com.example.dmc.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast // Pastikan ini diimpor
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dmc.ui.theme.DMCTheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userName = intent.getStringExtra("USER_NAME") ?: "Pasien"
        val userNim = intent.getStringExtra("USER_NIM") ?: ""
        val pasienId = intent.getLongExtra("PASIEN_ID", -1L)

        setContent {
            DMCTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    // Ambil applicationContext yang stabil DI SINI
                    val applicationContext = context.applicationContext // <-- Tambahkan baris ini

                    HomeScreen(
                        userName = userName,
                        onLogoutClick = {
                            // Gunakan applicationContext untuk Toast
                            Toast.makeText(applicationContext, "Anda telah Logout", Toast.LENGTH_SHORT).show()
                            val intent = Intent(context, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            context.startActivity(intent)
                        },
                        onViewMedicalRecordsClick = {
                            Toast.makeText(applicationContext, "Membuka Rekam Medis...", Toast.LENGTH_SHORT).show()
                            // TODO: Navigasi ke layar Rekam Medis
                        },
                        onAppointmentClick = {
                            Toast.makeText(applicationContext, "Membuka Buat Janji Temu...", Toast.LENGTH_SHORT).show()
                            // TODO: Navigasi ke layar Janji Temu
                        },
                        onMedicineInfoClick = {
                            Toast.makeText(applicationContext, "Membuka Informasi Obat...", Toast.LENGTH_SHORT).show()
                            // TODO: Navigasi ke layar Informasi Obat
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userName: String = "Pasien",
    onLogoutClick: () -> Unit = {},
    onViewMedicalRecordsClick: () -> Unit = {},
    onAppointmentClick: () -> Unit = {},
    onMedicineInfoClick: () -> Unit = {}
) {
    val context = LocalContext.current // Tetap ada untuk Intent dan DatePickerDialog jika dipakai
    // applicationContext sudah diambil di HomeActivity dan diberikan ke Composable ini melalui callback
    // Jadi Anda tidak perlu mendeklarasikannya lagi di sini jika semua Toast menggunakan callback

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Selamat Datang, ${userName.split(" ").firstOrNull() ?: "Pasien"}!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                actions = {
                    IconButton(onClick = {
                        // Gunakan callback yang sudah menerima context dari HomeActivity
                        Toast.makeText(context, "Informasi Aplikasi", Toast.LENGTH_SHORT).show() // Atau panggil fungsi info
                    }) {
                        Icon(Icons.Filled.Info, contentDescription = "Informasi Aplikasi")
                    }
                    IconButton(onClick = onLogoutClick) { // onLogoutClick sudah menangani Toast
                        Icon(Icons.Filled.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Apa yang ingin Anda lakukan hari ini?",
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Button(
                onClick = onViewMedicalRecordsClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Lihat Rekam Medis", fontSize = 18.sp)
            }

            Button(
                onClick = onAppointmentClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Buat Janji Temu", fontSize = 18.sp)
            }

            Button(
                onClick = onMedicineInfoClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
            ) {
                Text("Informasi Obat", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(64.dp))

            // Tombol Logout ini sudah tidak diperlukan jika ada ikon logout di TopAppBar
            // Namun, jika Anda ingin tetap ada, pastikan onClick-nya memanggil onLogoutClick()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreenWithAppBar() {
    DMCTheme {
        HomeScreen(userName = "Budi Santoso")
    }
}