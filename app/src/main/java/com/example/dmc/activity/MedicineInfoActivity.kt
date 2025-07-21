// src/main/java/com/example/dmc/activity/MedicineInfoActivity.kt
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
import androidx.compose.runtime.rememberCoroutineScope // Import ini
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dmc.api.Obat // Import data class Obat Anda
import com.example.dmc.ui.theme.DMCTheme
import com.example.dmc.util.RetrofitAuth // Import RetrofitAuth Anda
import kotlinx.coroutines.launch // Import ini
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MedicineInfoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DMCTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    val applicationContext = context.applicationContext

                    MedicineInfoScreen(
                        onBackClick = {
                            finish() // Kembali ke layar sebelumnya
                            Toast.makeText(applicationContext, "Kembali ke Home", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineInfoScreen(
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val applicationContext = context.applicationContext

    var obatList by remember { mutableStateOf<List<Obat>>(emptyList()) } // State untuk menyimpan data obat
    val coroutineScope = rememberCoroutineScope() // Diperlukan untuk meluncurkan coroutine

    // Panggil API saat Composable pertama kali masuk Composition
    LaunchedEffect(Unit) {
        coroutineScope.launch { // Meluncurkan coroutine di latar belakang
            try {
                // Memanggil API getObat dari RetrofitAuth.instance
                RetrofitAuth.instance.getObat().enqueue(object : Callback<List<Obat>> {
                    override fun onResponse(call: Call<List<Obat>>, response: Response<List<Obat>>) {
                        if (response.isSuccessful) {
                            obatList = response.body() ?: emptyList() // Update state dengan data dari API
                        } else {
                            val errorBody = response.errorBody()?.string()
                            Toast.makeText(applicationContext, "Gagal memuat obat: ${errorBody ?: "unknown error"}", Toast.LENGTH_LONG).show()
                            println("GET Obat Error: ${response.code()} - $errorBody")
                        }
                    }

                    override fun onFailure(call: Call<List<Obat>>, t: Throwable) {
                        Toast.makeText(applicationContext, "Error jaringan saat memuat obat: ${t.message}", Toast.LENGTH_LONG).show()
                        t.printStackTrace()
                    }
                })
            } catch (e: Exception) {
                Toast.makeText(applicationContext, "Exception saat memanggil API: ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Informasi Obat") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(obatList) { obat -> // Menggunakan obatList yang diambil dari API
                ObatCard(obat = obat)
            }
        }
    }
}

@Composable
fun ObatCard(obat: Obat) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = obat.namaObat,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = obat.deskripsi,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Stok: ${obat.stok}",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMedicineInfoScreen() {
    DMCTheme {
        MedicineInfoScreen()
    }
}