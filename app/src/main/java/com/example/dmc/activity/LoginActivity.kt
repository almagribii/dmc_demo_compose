import android.content.Intent
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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dmc.activity.RegisterActivity
import com.example.dmc.api.LoginRequest
import com.example.dmc.ui.theme.DMCTheme
import com.example.dmc.util.RetrofitAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun LoginActivty() {
    var nim by remember { mutableStateOf("") }
    var tanggalLahir by remember { mutableStateOf("") }
    var loginMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

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
            label = { Text("Username") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email Icon") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = tanggalLahir,
            onValueChange = { tanggalLahir = it },
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Lock Icon") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        )

        Button(
            onClick = {
                if (nim.isNotBlank() && tanggalLahir.isNotBlank()) {
                    val loginRequest = LoginRequest(nim, tanggalLahir)
                    RetrofitAuth.instance.login(loginRequest)
                        .enqueue(object : Callback<LoginRequest> {
                            override fun onResponse(
                                p0: Call<LoginRequest>,
                                p1: Response<LoginRequest>
                            ) {
                                if (p1.isSuccessful) {
                                    val lognRespone = p1.body()
                                    lognRespone?.let {
                                        loginMessage = "Login Berhasil! Selamat Datang"
                                    }
                                } else {
                                    val errorBody = p1.errorBody()?.string()
                                    loginMessage =
                                        "Login Gagal: ${errorBody ?: "Terjadi Kesalahan"}"
                                }
                            }

                            override fun onFailure(p0: Call<LoginRequest>, p1: Throwable) {
                                loginMessage = "Error Jaringan: ${p1.message}"
                            }
                        })
                } else {
                    loginMessage = "NIM dan Tanggal Lahir tidak boleh Kosong"
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
                .fillMaxWidth() // Memenuhi seluruh lebar yang tersedia
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround, // Memberi jarak merata di antara elemen
            verticalAlignment = Alignment.CenterVertically // Elemen sejajar di tengah secara vertikal
        ) {
            Text(
                text = "Apakah Anda Baru Pertama Kali Ke DMC",
                fontSize = 12.sp,
                modifier = Modifier.clickable {
                    val intent = Intent(context, RegisterActivity::class.java)
                    context.startActivity(intent)
                })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    DMCTheme {
        LoginActivty()
    }
}