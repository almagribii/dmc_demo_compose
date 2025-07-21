package com.example.dmc.activity

import android.content.Intent
import android.os.Bundle
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
import androidx.compose.material.icons.filled.Person // Menggunakan Person untuk NIM
import androidx.compose.material.icons.filled.Lock // Menggunakan Lock untuk password
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.input.PasswordVisualTransformation // Diperlukan untuk password
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dmc.api.LoginRequest
import com.example.dmc.api.LoginResponse
import com.example.dmc.ui.theme.DMCTheme
import com.example.dmc.util.RetrofitAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DMCTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen() {
    var nim by remember { mutableStateOf("") }
    var tanggalLahir by remember { mutableStateOf("") } // Untuk tampilan String YYYY-MM-DD
    var loginMessage by remember { mutableStateOf("") }
    val context = LocalContext.current
    val applicationContext = context.applicationContext // Ambil applicationContext yang stabil

    // DatePicker setup DIKOMENTARI
    // val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE // "YYYY-MM-DD"
    // val year: Int
    // val month: Int
    // val day: Int
    // val calendar = Calendar.getInstance()
    // year = calendar.get(Calendar.YEAR)
    // month = calendar.get(Calendar.MONTH)
    // day = calendar.get(Calendar.DAY_OF_MONTH)

    // val datePickerDialog = DatePickerDialog(
    //     context,
    //     { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
    //         tanggalLahir = LocalDate.of(selectedYear, selectedMonth + 1, selectedDayOfMonth)
    //             .format(dateFormatter)
    //     }, year, month, day
    // )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Login",
            fontSize = 32.sp,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = nim,
            onValueChange = { nim = it },
            label = { Text("NIM (Username)") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = "NIM Icon") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = tanggalLahir,
            onValueChange = { tanggalLahir = it },
            label = { Text("Tanggal Lahir (Password - YYYY-MM-DD)") }, // Label tetap informatif
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password Icon") }, // Ikon Lock
            visualTransformation = PasswordVisualTransformation(), // Tetap disembunyikan
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), // Keyboard password
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
            // .clickable { datePickerDialog.show() } // DIKOMENTARI
        )

        Button(
            onClick = {
                if (nim.isNotBlank() && tanggalLahir.isNotBlank()) {
                    val request = LoginRequest(nim, tanggalLahir)
                    RetrofitAuth.instance.login(request).enqueue(object : Callback<LoginResponse> {
                        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                            if (response.isSuccessful) {
                                val loginResponse = response.body()
                                loginResponse?.let {
                                    Toast.makeText(applicationContext, "Login Berhasil: ${it.message}", Toast.LENGTH_LONG).show()

                                    // Debugging: Cetak nilai pasienId sebelum dikirim
                                    println("DEBUG_ID: LoginActivity mengirim pasienId: ${it.pasienId}")

                                    val intent = Intent(context, HomeActivity::class.java).apply {
                                        putExtra("USER_NAME", it.nama)
                                        putExtra("USER_NIM", it.nim)
                                        putExtra("PASIEN_ID", it.pasienId) // Kuncinya "PASIEN_ID"
                                    }
                                    context.startActivity(intent)
                                    (context as? ComponentActivity)?.finish()
                                }
                            } else {
                                val errorBody = response.errorBody()?.string()
                                val errorMessage = try {
                                    val errorMap = com.google.gson.Gson().fromJson(errorBody, Map::class.java)
                                    errorMap?.get("message") as? String ?: "Login gagal. Terjadi kesalahan."
                                } catch (e: Exception) {
                                    "Login gagal. Silakan coba lagi."
                                }
                                Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_LONG).show()
                                println("Login Error Body: $errorBody")
                            }
                        }

                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                            Toast.makeText(applicationContext, "Error Jaringan: ${t.message}", Toast.LENGTH_LONG).show()
                            t.printStackTrace()
                        }
                    })
                } else {
                    Toast.makeText(applicationContext, "NIM dan Tanggal Lahir tidak boleh kosong", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = loginMessage, color = MaterialTheme.colorScheme.error)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Don't have an account? Register here.",
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    val intent = Intent(context, RegisterActivity::class.java)
                    context.startActivity(intent)
                    (context as? ComponentActivity)?.finish()
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    DMCTheme {
        LoginScreen()
    }
}