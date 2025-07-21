package com.example.dmc.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MedicalServices // Ikon untuk nama obat
import androidx.compose.material.icons.filled.Numbers // Ikon untuk kuantitas
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dmc.api.Obat // Import data class Obat
import com.example.dmc.ui.theme.DMCTheme
import com.example.dmc.util.RetrofitAuth // Import RetrofitAuth
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * DispenseMedicineActivity adalah layar untuk melakukan pemberian obat (mengurangi stok).
 * Pengguna akan memilih Nama Obat dari dropdown dan memasukkan Kuantitas.
 */
class DispenseMedicineActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DMCTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DispenseMedicineScreen(
                        onBackClick = { finish() }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class) // Diperlukan untuk ExposedDropdownMenuBox
@Composable
fun DispenseMedicineScreen(
    onBackClick: () -> Unit = {}
) {
    var selectedObatText by remember { mutableStateOf("") } // Teks yang ditampilkan di TextField dropdown
    var selectedObatObject by remember { mutableStateOf<Obat?>(null) } // Objek Obat yang dipilih
    var quantityInput by remember { mutableStateOf("") }
    var dispenseMessage by remember { mutableStateOf("") }

    var allObatList by remember { mutableStateOf<List<Obat>>(emptyList()) }
    var isObatListLoading by remember { mutableStateOf(true) }
    var obatListErrorMessage by remember { mutableStateOf<String?>(null) }

    var expanded by remember { mutableStateOf(false) } // State untuk mengontrol dropdown expanded/collapsed

    val context = LocalContext.current
    val applicationContext = context.applicationContext
    val coroutineScope = rememberCoroutineScope()

    // LaunchedEffect untuk mengambil daftar semua obat saat layar dimuat
    LaunchedEffect(Unit) {
        isObatListLoading = true
        obatListErrorMessage = null
        RetrofitAuth.instance.getObat().enqueue(object : Callback<List<Obat>> {
            override fun onResponse(call: Call<List<Obat>>, response: Response<List<Obat>>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    allObatList = body ?: emptyList()
                    if (allObatList.isEmpty()) {
                        obatListErrorMessage = "Tidak ada obat ditemukan di database. Tambahkan obat terlebih dahulu."
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    obatListErrorMessage = "Gagal memuat daftar obat: ${errorBody ?: "unknown error"}"
                    Toast.makeText(applicationContext, obatListErrorMessage ?: "Error", Toast.LENGTH_LONG).show()
                }
                isObatListLoading = false
            }

            override fun onFailure(call: Call<List<Obat>>, t: Throwable) {
                obatListErrorMessage = "Error jaringan saat memuat daftar obat: ${t.message}"
                Toast.makeText(applicationContext, obatListErrorMessage ?: "Error", Toast.LENGTH_LONG).show()
                t.printStackTrace()
                isObatListLoading = false
            }
        })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pemberian Obat") },
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
                .padding(16.dp)
                .offset(y = (-30).dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Form Pemberian Obat",
                fontSize = 22.sp,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Tampilkan pesan loading/error untuk daftar obat
            if (isObatListLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                Text("Memuat daftar obat...", modifier = Modifier.padding(top = 8.dp))
            } else if (obatListErrorMessage != null) {
                Text(obatListErrorMessage!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
            }

            // --- ExposedDropdownMenuBox untuk memilih Nama Obat ---
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedObatText,
                    onValueChange = { selectedObatText = it },
                    readOnly = true, // Membuat TextField tidak bisa diketik langsung
                    label = { Text("Pilih Nama Obat") },
                    leadingIcon = { Icon(Icons.Default.MedicalServices, contentDescription = "Nama Obat Icon") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .menuAnchor() // Harus ada untuk ExposedDropdownMenuBox
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    allObatList.forEach { obat ->
                        DropdownMenuItem(
                            text = { Text(obat.namaObat) },
                            onClick = {
                                selectedObatText = obat.namaObat
                                selectedObatObject = obat // Simpan objek obat yang dipilih
                                expanded = false
                                dispenseMessage = "" // Hapus pesan error sebelumnya
                            }
                        )
                    }
                }
            }
            // --- Akhir ExposedDropdownMenuBox ---

            // Input Kuantitas
            OutlinedTextField(
                value = quantityInput,
                onValueChange = { newValue ->
                    quantityInput = newValue.filter { it.isDigit() }
                },
                label = { Text("Kuantitas") },
                leadingIcon = { Icon(Icons.Default.Numbers, contentDescription = "Quantity Icon") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Tombol Berikan Obat
            Button(
                onClick = {
                    val quantity = quantityInput.toIntOrNull()

                    // Validasi: Obat harus dipilih dan kuantitas valid
                    if (selectedObatObject == null || quantity == null || quantity <= 0) {
                        Toast.makeText(applicationContext, "Pilih Obat dan masukkan Kuantitas positif!", Toast.LENGTH_LONG).show()
                        dispenseMessage = "Pilih Obat dan masukkan Kuantitas positif!"
                    } else {
                        // Panggil API dispenseObat dengan ID dari objek obat yang dipilih
                        RetrofitAuth.instance.dispenseObat(selectedObatObject!!.id, quantity).enqueue(object : Callback<Obat> {
                            override fun onResponse(call: Call<Obat>, response: Response<Obat>) {
                                if (response.isSuccessful) {
                                    val updatedObat = response.body()
                                    updatedObat?.let {
                                        Toast.makeText(applicationContext, "Obat ${it.namaObat} berhasil diberikan. Stok tersisa: ${it.stok}", Toast.LENGTH_LONG).show()
                                        dispenseMessage = "Berhasil: Stok ${it.namaObat} sisa ${it.stok}"
                                        // Opsional: Kosongkan input setelah berhasil
                                        selectedObatText = ""
                                        selectedObatObject = null
                                        quantityInput = ""
                                    }
                                } else {
                                    val errorBody = response.errorBody()?.string()
                                    val errorMessage = try {
                                        val errorMap = com.google.gson.Gson().fromJson(errorBody, Map::class.java)
                                        errorMap?.get("error") as? String ?: "Gagal memberikan obat. Terjadi kesalahan."
                                    } catch (e: Exception) {
                                        "Gagal memberikan obat. Silakan coba lagi."
                                    }
                                    Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_LONG).show()
                                    dispenseMessage = errorMessage
                                    println("Dispense Obat Error: ${response.code()} - $errorBody")
                                }
                            }

                            override fun onFailure(call: Call<Obat>, t: Throwable) {
                                Toast.makeText(applicationContext, "Error jaringan: ${t.message}", Toast.LENGTH_LONG).show()
                                dispenseMessage = "Error jaringan: ${t.message}"
                                t.printStackTrace()
                            }
                        })
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Berikan Obat", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = dispenseMessage, color = MaterialTheme.colorScheme.error)
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PreviewDispenseMedicineScreen() {
    DMCTheme {
        DispenseMedicineScreen()
    }
}