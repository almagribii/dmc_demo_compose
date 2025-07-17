package com.example.dmc.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dmc.R
import com.example.dmc.ui.theme.DMCTheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Di sinilah Compose UI dimulai untuk Activity ini.
        // setContent adalah fungsi non-Composable yang menerima Composable lambda.
        setContent {
            // Di sini Anda memanggil fungsi Composable utama untuk layar ini
            DMCTheme { // Memanggil tema Composable Anda
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen() // Ini adalah panggilan fungsi @Composable yang benar
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0F0)) // Latar belakang abu-abu muda di luar card
            .padding(16.dp), // Padding untuk memberikan ruang di tepi layar
        contentAlignment = Alignment.Center // Pusatkan seluruh konten (gambar + card)
    ) {
        // 1. Gambar Orang Naik Skuter (Lapisan Bawah, tapi akan di-offset ke kiri atas)
        // Pastikan Anda memiliki resource gambar ini di res/drawable
        // Misalnya: scooter_person.png-
        Image(
            painter = painterResource(id = R.drawable.person), // GANTI DENGAN NAMA GAMBAR ANDA
            contentDescription = "Person on scooter",
            modifier = Modifier
                .size(250.dp) // Sesuaikan ukuran gambar
                .align(Alignment.CenterStart) // Awalnya sejajar kiri tengah dari Box
                .offset(x = (-60).dp, y = (-120).dp), // <-- PENTING: Geser keluar dari Card
            contentScale = ContentScale.Fit
        )

        // 2. Card Login (Lapisan Atas)
        Card(
            modifier = Modifier
                .width(360.dp) // Sesuaikan lebar card agar gambar bisa di-offset
                .height(480.dp) // Sesuaikan tinggi card
                .padding(start = 80.dp), // <-- PENTING: Beri padding di awal untuk memberi ruang gambar
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface) // Warna putih untuk card
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp), // Padding di dalam card
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Welcome Text
                Text(
                    text = "Welcome to",
                    fontSize = 18.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.Start)
                )
                Text(
                    text = "ONN Bikes",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFA500), // Warna oranye
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 24.dp)
                )

                // Username/Email Input
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Enter Username or Email Address") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                // Password Input
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Enter Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                // Login Button
                Button(
                    onClick = {
                        if (username == "user" && password == "pass") {
                            Toast.makeText(context, "Login Berhasil!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Username atau Password salah", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA500)), // Oranye
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("LOG IN", color = Color.White, fontSize = 16.sp)
                }

                // Forgot Password
                Text(
                    text = "Forgot password?",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(Alignment.End) // Sejajar kanan dari kolom
                        .padding(top = 8.dp)
                        .clickable {
                            Toast.makeText(context, "Forgot password clicked", Toast.LENGTH_SHORT).show()
                        }
                )

                // Or Log in with
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Divider(modifier = Modifier.weight(1f), color = Color.LightGray)
                    Text(
                        text = " Or Log in with ",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    Divider(modifier = Modifier.weight(1f), color = Color.LightGray)
                }

                // Social Login Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Google Button
                    Button(
                        onClick = { Toast.makeText(context, "Google Login", Toast.LENGTH_SHORT).show() },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .padding(end = 8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        border = ButtonDefaults.outlinedButtonBorder,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(0.dp) // Hapus padding internal default
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_google), // GANTI DENGAN NAMA GAMBAR GOOGLE ANDA
                            contentDescription = "Google Icon",
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Facebook Button
                    Button(
                        onClick = { Toast.makeText(context, "Facebook Login", Toast.LENGTH_SHORT).show() },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .padding(start = 8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        border = ButtonDefaults.outlinedButtonBorder,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(0.dp) // Hapus padding internal default
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_facebook), // GANTI DENGAN NAMA GAMBAR FACEBOOK ANDA
                            contentDescription = "Facebook Icon",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // Create Account Link
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Create my ONN Bikes account!",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        Toast.makeText(context, "Create account clicked", Toast.LENGTH_SHORT).show()
                        // Intent untuk RegisterActivity jika ada
                        // val intent = Intent(context, RegisterActivity::class.java)
                        // context.startActivity(intent)
                    }
                )
            }
        }
    }
}
