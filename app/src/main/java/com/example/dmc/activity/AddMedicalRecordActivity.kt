// src/main/java/com/example/dmc/activity/AddMedicalRecordActivity.kt
package com.example.dmc.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
// Import ikon-ikon spesifik untuk form rekam medis
import androidx.compose.material.icons.filled.Description // Untuk Keluhan
import androidx.compose.material.icons.filled.MedicalServices // Untuk Diagnosa
import androidx.compose.material.icons.filled.Build // Untuk Tindakan (atau Healing)
import androidx.compose.material.icons.filled.ReceiptLong // Untuk Resep

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dmc.api.RekamMedis // Import RekamMedis data class
import com.example.dmc.ui.theme.DMCTheme
import com.example.dmc.util.RetrofitAuth // Import RetrofitAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * AddMedicalRecordActivity adalah Activity untuk menambahkan entri rekam medis baru.
 * Menerima ID dan nama pasien dari Activity sebelumnya (HomeActivity).
 */
class AddMedicalRecordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ambil ID Pasien dari Intent
        val pasienId = intent.getLongExtra("PASIEN_ID", -1L) // Default -1L jika tidak ada ID
        // Ambil Nama Pasien dari Intent
        val patientName = intent.getStringExtra("PATIENT_NAME") ?: "Pasien" // Default "Pasien" jika tidak ada nama

        setContent {
            DMCTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Hanya tampilkan layar jika ID pasien valid
                    if (pasienId != -1L) {
                        AddMedicalRecordScreen(
                            pasienId = pasienId,
                            patientName = patientName, // Teruskan nama pasien ke Composable
                            onBackClick = { finish() } // Callback untuk kembali
                        )
                    } else {
                        // Tampilkan pesan error jika ID pasien tidak ditemukan
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Error: ID Pasien tidak ditemukan.", color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Composable untuk UI form penambahan rekam medis.
 */
@OptIn(ExperimentalMaterial3Api::class) // Diperlukan untuk komponen Material3
@Composable
fun AddMedicalRecordScreen(
    pasienId: Long, // ID Pasien
    patientName: String = "Pasien", // Nama Pasien (untuk tampilan di UI)
    onBackClick: () -> Unit = {} // Callback untuk tombol kembali
) {
    // States untuk menyimpan inputan form
    var keluhan by remember { mutableStateOf("") }
    var diagnosa by remember { mutableStateOf("") }
    var tindakan by remember { mutableStateOf("") }
    var resep by remember { mutableStateOf("") }
    var submitMessage by remember { mutableStateOf("") } // Pesan feedback setelah submit

    val context = LocalContext.current
    val applicationContext = context.applicationContext // Dapatkan applicationContext yang stabil

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tambah Rekam Medis") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Terapkan padding dari Scaffold
                .padding(16.dp) // Padding tambahan untuk konten form
                .offset(y = (-30).dp), // Sesuaikan posisi kolom sedikit ke atas
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Judul form dengan nama pasien
            Text(
                text = "Form Rekam Medis Pasien: $patientName (ID: $pasienId)",
                fontSize = 22.sp,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Input Keluhan
            OutlinedTextField(
                value = keluhan,
                onValueChange = { keluhan = it },
                label = { Text("Keluhan") },
                leadingIcon = { Icon(Icons.Filled.Description, contentDescription = "Keluhan Icon") }, // Ikon Description
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 60.dp) // Minimal tinggi TextField
                    .padding(vertical = 8.dp)
            )

            // Input Diagnosa
            OutlinedTextField(
                value = diagnosa,
                onValueChange = { diagnosa = it },
                label = { Text("Diagnosa") },
                leadingIcon = { Icon(Icons.Filled.MedicalServices, contentDescription = "Diagnosa Icon") }, // Ikon MedicalServices
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 60.dp)
                    .padding(vertical = 8.dp)
            )

            // Input Tindakan
            OutlinedTextField(
                value = tindakan,
                onValueChange = { tindakan = it },
                label = { Text("Tindakan") },
                leadingIcon = { Icon(Icons.Filled.Build, contentDescription = "Tindakan Icon") }, // Ikon Build
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 60.dp)
                    .padding(vertical = 8.dp)
            )

            // Input Resep
            OutlinedTextField(
                value = resep,
                onValueChange = { resep = it },
                label = { Text("Resep") },
                leadingIcon = { Icon(Icons.Filled.ReceiptLong, contentDescription = "Resep Icon") }, // Ikon ReceiptLong
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 60.dp)
                    .padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Tombol Submit
            Button(
                onClick = {
                    // Validasi input form
                    if (keluhan.isBlank() || diagnosa.isBlank() || tindakan.isBlank() || resep.isBlank()) {
                        Toast.makeText(applicationContext, "Harap lengkapi semua bidang!", Toast.LENGTH_SHORT).show()
                        submitMessage = "Harap lengkapi semua bidang!"
                    } else {
                        // Buat objek RekamMedis dari inputan
                        val rekamMedis = RekamMedis(
                            keluhan = keluhan,
                            diagnosa = diagnosa,
                            tindakan = tindakan,
                            resep = resep
                        )

                        // Panggil API untuk menambahkan rekam medis (asinkron)
                        RetrofitAuth.instance.addRekamMedis(pasienId, rekamMedis).enqueue(object : Callback<RekamMedis> {
                            override fun onResponse(call: Call<RekamMedis>, response: Response<RekamMedis>) {
                                if (response.isSuccessful) {
                                    Toast.makeText(applicationContext, "Rekam Medis berhasil ditambahkan!", Toast.LENGTH_LONG).show()
                                    submitMessage = "Rekam Medis berhasil ditambahkan!"
                                    onBackClick() // Kembali ke layar sebelumnya setelah berhasil
                                } else {
                                    val errorBody = response.errorBody()?.string()
                                    val errorMessage = try {
                                        val errorMap = com.google.gson.Gson().fromJson(errorBody, Map::class.java)
                                        errorMap?.get("error") as? String ?: "Gagal menambahkan rekam medis. Terjadi kesalahan."
                                    } catch (e: Exception) {
                                        "Gagal menambahkan rekam medis. Silakan coba lagi."
                                    }
                                    Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_LONG).show()
                                    submitMessage = errorMessage
                                    println("Add Rekam Medis Error: ${response.code()} - $errorBody")
                                }
                            }

                            override fun onFailure(call: Call<RekamMedis>, t: Throwable) {
                                Toast.makeText(applicationContext, "Error jaringan: ${t.message}", Toast.LENGTH_LONG).show()
                                submitMessage = "Error jaringan: ${t.message}"
                                t.printStackTrace()
                            }
                        })
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Simpan Rekam Medis", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = submitMessage, color = MaterialTheme.colorScheme.error)
        }
    }
}

/**
 * Preview untuk AddMedicalRecordScreen Composable.
 */
@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PreviewAddMedicalRecordScreen() {
    DMCTheme {
        // ID Pasien dan nama dummy untuk preview
        AddMedicalRecordScreen(pasienId = 123L, patientName = "Nama Pasien Contoh")
    }
}