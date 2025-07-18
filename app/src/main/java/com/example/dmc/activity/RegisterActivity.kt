package com.example.dmc.activity

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dmc.api.Pasien // Import Pasien data class Anda
import com.example.dmc.api.RegisterResponse // Import RegisterResponse (jika Anda pakai)
import com.example.dmc.ui.theme.DMCTheme
import com.example.dmc.util.RetrofitAuth // Import RetrofitAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DMCTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RegisterScreen()
                }
            }
        }
    }
}

// src/main/java/com/example/dmc/activity/RegisterActivity.kt
// ... (imports lainnya) ...

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen() {
    var namaLengkap by remember { mutableStateOf("") }
    var tanggalLahir by remember { mutableStateOf("") }
    var alamat by remember { mutableStateOf("") }
    var nomorTelepon by remember { mutableStateOf("") }
    var isMahasiswaUnida by remember { mutableStateOf(false) }
    var nim by remember { mutableStateOf("") } // <-- Tetap String non-nullable
    var registerMessage by remember { mutableStateOf("") }

    val context = LocalContext.current
//    val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    val year: Int
    val month: Int
    val day: Int
    val calendar = Calendar.getInstance()
    year = calendar.get(Calendar.YEAR)
    month = calendar.get(Calendar.MONTH)
    day = calendar.get(Calendar.DAY_OF_MONTH)

//    val datePickerDialog = DatePickerDialog(
//        context,
//        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
//            tanggalLahir = LocalDate.of(selectedYear, selectedMonth + 1, selectedDayOfMonth)
//                .format(dateFormatter)
//        }, year, month, day
//    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Register Pasien",
            fontSize = 32.sp
        )
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = namaLengkap,
            onValueChange = { namaLengkap = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            label = { Text("Nama Lengkap") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Person Icon") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )

        OutlinedTextField(
            value = tanggalLahir,
            onValueChange = { tanggalLahir = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            label = { Text("Tanggal Lahir (YYYY-MM-DD)") },
            leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = "Date Icon") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
//                .clickable { datePickerDialog.show() }
        )

        OutlinedTextField(
            value = alamat,
            onValueChange = { alamat = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            label = { Text("Alamat Rumah") },
            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = "Location Icon") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )

        OutlinedTextField(
            value = nomorTelepon,
            onValueChange = { nomorTelepon = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            label = { Text("Nomor Telepon") },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = "Phone Icon") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Apakah Anda Mahasiswa Unida?",
                style = MaterialTheme.typography.bodyLarge
            )
            Switch(
                checked = isMahasiswaUnida,
                onCheckedChange = { isMahasiswaUnida = it
                    if (!it) { // Jika switch dimatikan, kosongkan NIM
                        nim = ""
                    }
                }
            )
        }

        // Input NIM (Hanya muncul jika isMahasiswaUnida true)
        if (isMahasiswaUnida) {
            OutlinedTextField(
                value = nim, // Menggunakan state 'nim' yang sama
                onValueChange = { nim = it }, // Update state 'nim'
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                label = { Text("NIM") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = "NIM Icon") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                // Validasi yang lebih ketat: NIM tidak boleh kosong jika isMahasiswaUnida true
                if (namaLengkap.isBlank() || tanggalLahir.isBlank() || alamat.isBlank() || nomorTelepon.isBlank() || (isMahasiswaUnida && nim.isBlank())) {
                    Toast.makeText(context, "Harap lengkapi semua data dan NIM jika Anda mahasiswa!", Toast.LENGTH_LONG).show()
                    registerMessage = "Harap lengkapi semua data dan NIM jika Anda mahasiswa!"
                } else {
                    val newPasien = Pasien(
                        nama = namaLengkap,
                        tanggalLahir = tanggalLahir,
                        alamat = alamat,
                        nomorTelepon = nomorTelepon,
                        isMahasiswaUnida = isMahasiswaUnida,
                        // Jika isMahasiswaUnida false, kirim nim sebagai string kosong ""
                        nim = if (isMahasiswaUnida) nim else ""
                    )

                    RetrofitAuth.instance.registerPasien(newPasien).enqueue(object : Callback<Pasien> {
                        override fun onResponse(call: Call<Pasien>, response: Response<Pasien>) {
                            if (response.isSuccessful) {
                                val registeredPasien = response.body()
                                registeredPasien?.let {
                                    Toast.makeText(context, "Registrasi Berhasil! ID Pasien: ${it.id}", Toast.LENGTH_LONG).show()
                                    registerMessage = "Registrasi Berhasil!"
                                    val intent = Intent(context, LoginActivity::class.java)
                                    context.startActivity(intent)
                                    (context as? ComponentActivity)?.finish()
                                }
                            } else {
                                val errorBody = response.errorBody()?.string()
                                val errorMessage = try {
                                    val errorMap = com.google.gson.Gson().fromJson(errorBody, Map::class.java)
                                    errorMap?.get("error") as? String ?: "Registrasi gagal. Terjadi kesalahan."
                                } catch (e: Exception) {
                                    "Registrasi gagal. Server tidak merespons dengan benar."
                                }
                                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                                registerMessage = errorMessage
                                println("Register Error: ${response.code()} - $errorBody")
                            }
                        }

                        override fun onFailure(call: Call<Pasien>, t: Throwable) {
                            Toast.makeText(context, "Error jaringan: ${t.message}", Toast.LENGTH_LONG).show()
                            registerMessage = "Error jaringan: ${t.message}"
                            t.printStackTrace()
                        }
                    })
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("REGISTER")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = registerMessage, color = MaterialTheme.colorScheme.error)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Already have an account? Login here.",
            color = MaterialTheme.colorScheme.primary,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable {
                val intent = Intent(context, LoginActivity::class.java)
                context.startActivity(intent)
                (context as? ComponentActivity)?.finish()
            }
        )
    }
}