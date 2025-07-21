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
//import androidx.compose.material.icons.filled.Description
//import androidx.compose.material.icons.filled.LocalHospital
//import androidx.compose.material.icons.filled.MedicalServices
//import androidx.compose.material.icons.filled.Note
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

class AddMedicalRecordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ambil pasienId dari Intent
        val pasienId = intent.getLongExtra("PASIEN_ID", -1L) // Default -1L jika tidak ada ID

        setContent {
            DMCTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (pasienId != -1L) {
                        AddMedicalRecordScreen(
                            pasienId = pasienId,
                            onBackClick = { finish() }
                        )
                    } else {
                        // Tampilkan pesan error jika ID pasien tidak valid
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Error: ID Pasien tidak ditemukan.", color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicalRecordScreen(
    pasienId: Long, // Terima ID Pasien sebagai parameter
    onBackClick: () -> Unit = {}
) {
    var keluhan by remember { mutableStateOf("") }
    var diagnosa by remember { mutableStateOf("") }
    var tindakan by remember { mutableStateOf("") }
    var resep by remember { mutableStateOf("") }
    var submitMessage by remember { mutableStateOf("") }

    val context = LocalContext.current
    val applicationContext = context.applicationContext

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
                .offset(y = (-30).dp), // Sesuaikan posisi ke atas
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Form Rekam Medis Pasien ID: $pasienId",
                fontSize = 22.sp,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Input Keluhan
            OutlinedTextField(
                value = keluhan,
                onValueChange = { keluhan = it },
                label = { Text("Keluhan") },
//                leadingIcon = { Icon(Icons.Default.Description, contentDescription = "Keluhan Icon") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 60.dp) // Minimal tinggi
                    .padding(vertical = 8.dp)
            )

            // Input Diagnosa
            OutlinedTextField(
                value = diagnosa,
                onValueChange = { diagnosa = it },
                label = { Text("Diagnosa") },
//                leadingIcon = { Icon(Icons.Default.MedicalServices, contentDescription = "Diagnosa Icon") },
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
//                leadingIcon = { Icon(Icons.Default.LocalHospital, contentDescription = "Tindakan Icon") },
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
//                leadingIcon = { Icon(Icons.Default.Note, contentDescription = "Resep Icon") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 60.dp)
                    .padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Tombol Submit
            Button(
                onClick = {
                    if (keluhan.isBlank() || diagnosa.isBlank() || tindakan.isBlank() || resep.isBlank()) {
                        Toast.makeText(applicationContext, "Harap lengkapi semua bidang!", Toast.LENGTH_SHORT).show()
                        submitMessage = "Harap lengkapi semua bidang!"
                    } else {
                        val rekamMedis = RekamMedis(
                            keluhan = keluhan,
                            diagnosa = diagnosa,
                            tindakan = tindakan,
                            resep = resep
                        )

                        // Panggil API untuk menambahkan rekam medis
                        RetrofitAuth.instance.addRekamMedis(pasienId, rekamMedis).enqueue(object : Callback<RekamMedis> {
                            override fun onResponse(call: Call<RekamMedis>, response: Response<RekamMedis>) {
                                if (response.isSuccessful) {
                                    Toast.makeText(applicationContext, "Rekam Medis berhasil ditambahkan!", Toast.LENGTH_LONG).show()
                                    submitMessage = "Rekam Medis berhasil ditambahkan!"
                                    // Opsional: Kembali ke Home Screen atau tampilkan detail rekam medis yang baru
                                    onBackClick() // Kembali ke layar sebelumnya
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

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PreviewAddMedicalRecordScreen() {
    DMCTheme {
        // ID Pasien dummy untuk preview
        AddMedicalRecordScreen(pasienId = 123L)
    }
}