package com.example.dmc.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dmc.api.RekamMedis // Import RekamMedis data class
import com.example.dmc.ui.theme.DMCTheme
import com.example.dmc.util.RetrofitAuth // Import RetrofitAuth
import kotlinx.coroutines.launch // Import ini untuk coroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * MedicalRecordsListActivity menampilkan daftar rekam medis untuk pasien tertentu.
 * ID pasien diteruskan melalui Intent.
 */
class MedicalRecordsListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ambil ID Pasien dari Intent
        val pasienId = intent.getLongExtra("PASIEN_ID", -1L) // Default -1L jika ID tidak ada

        // Debugging: Cetak nilai pasienId yang diterima MedicalRecordsListActivity
        println("DEBUG_ID: MedicalRecordsListActivity menerima pasienId: $pasienId")

        setContent {
            DMCTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (pasienId != -1L) {
                        MedicalRecordsListScreen(
                            pasienId = pasienId,
                            onBackClick = { finish() }
                        )
                    } else {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                            Text("Error: ID Pasien tidak ditemukan untuk melihat rekam medis.", color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Composable untuk menampilkan UI daftar rekam medis.
 * Melakukan panggilan API untuk mengambil data rekam medis.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicalRecordsListScreen(
    pasienId: Long,
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val applicationContext = context.applicationContext

    var medicalRecords by remember { mutableStateOf<List<RekamMedis>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // LaunchedEffect akan memicu panggilan API saat Composable pertama kali muncul
    // atau ketika pasienId berubah
    LaunchedEffect(pasienId) {
        isLoading = true // Set status loading ke true
        errorMessage = null // Reset pesan error
        medicalRecords = emptyList() // Kosongkan daftar sebelumnya

        // --- PERBAIKAN DI SINI: Gunakan enqueue() untuk panggilan API asinkron ---
        RetrofitAuth.instance.getRekamMedisByPasienId(pasienId).enqueue(object : Callback<List<RekamMedis>> {
            override fun onResponse(call: Call<List<RekamMedis>>, response: Response<List<RekamMedis>>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        medicalRecords = body // Update state dengan data dari API
                        if (medicalRecords.isEmpty()) {
                            errorMessage = "Tidak ada rekam medis ditemukan untuk pasien ini."
                        }
                    } else {
                        errorMessage = "Respons dari server kosong."
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    errorMessage = "Gagal memuat rekam medis: ${errorBody ?: "unknown error"}. Status: ${response.code()}"
                    Toast.makeText(applicationContext, errorMessage ?: "Error tidak diketahui", Toast.LENGTH_LONG).show()
                    println("GET Rekam Medis Error: ${response.code()} - $errorBody")
                }
                isLoading = false // Selesai loading, baik sukses atau gagal
            }

            override fun onFailure(call: Call<List<RekamMedis>>, t: Throwable) {
                errorMessage = "Error jaringan: ${t.message}"
                Toast.makeText(applicationContext, errorMessage ?: "Error jaringan tidak diketahui", Toast.LENGTH_LONG).show()
                t.printStackTrace() // Cetak stack trace ke Logcat
                isLoading = false // Selesai loading karena gagal
            }
        })
        // --- AKHIR PERBAIKAN ---
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Jejak Rekam Medis") },
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
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Tampilkan indikator loading jika data sedang dimuat
            if (isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                Text("Memuat rekam medis...", modifier = Modifier.padding(top = 16.dp), style = MaterialTheme.typography.bodyMedium)
            }
            // Tampilkan pesan error jika ada
            else if (errorMessage != null) {
                Text(errorMessage!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 16.dp), style = MaterialTheme.typography.bodyLarge)
            }
            // Tampilkan pesan jika tidak ada rekam medis ditemukan (setelah loading dan tidak ada error)
            else if (medicalRecords.isEmpty()) {
                Text("Belum ada rekam medis untuk pasien ini.", modifier = Modifier.padding(top = 16.dp), style = MaterialTheme.typography.bodyLarge)
            }
            // Tampilkan daftar rekam medis jika ada data
            else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(medicalRecords) { record ->
                        MedicalRecordCard(record = record)
                    }
                }
            }
        }
    }
}

/**
 * Composable untuk menampilkan detail satu entri rekam medis dalam Card.
 * Menangani tanggal sebagai String?
 */
@Composable
fun MedicalRecordCard(record: RekamMedis) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Keluhan: ${record.keluhan}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Diagnosa: ${record.diagnosa}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Tindakan: ${record.tindakan}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Resep: ${record.resep}",
                style = MaterialTheme.typography.bodyMedium
            )
            // Tampilkan tanggal konsultasi, ambil hanya bagian tanggalnya jika ada 'T'
            Text(
                text = "Tanggal Konsultasi: ${record.tanggalKonsultasi?.split("T")?.firstOrNull() ?: "Tanggal tidak tersedia"}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            // Contoh untuk menampilkan createdAt jika diperlukan
            // Text("Dibuat: ${record.createdAt?.split("T")?.firstOrNull() ?: "N/A"}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PreviewMedicalRecordsListScreen() {
    DMCTheme {
        MedicalRecordsListScreen(pasienId = 1L)
    }
}